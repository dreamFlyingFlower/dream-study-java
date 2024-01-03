package com.wy.minio.upload;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListPartsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.PartSummary;
import com.wy.digest.DigestHelper;
import com.wy.minio.MinioProperties;
import com.wy.util.DateHelper;

import lombok.AllArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:13:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class FileTaskService {

	/** 预签名url过期时间(ms) */
	private static final Long PRE_SIGN_URL_EXPIRE = 60 * 10 * 1000L;

	private final FileTaskMapper fileTaskMapper;

	private final AmazonS3 amazonS3;

	private final MinioProperties minioProperties;

	public FileTaskDTO getTaskInfo(String md5) {
		FileTaskEntity task = getByMd5(md5);
		if (task == null) {
			return null;
		}
		FileTaskDTO result = FileTaskDTO.builder().finished(true).taskRecord(FileTaskRecordDTO.convertFromEntity(task))
				.path(getPath(task.getBucketName(), task.getObjectKey())).build();

		boolean doesObjectExist = amazonS3.doesObjectExist(task.getBucketName(), task.getObjectKey());
		if (!doesObjectExist) {
			// 未上传完,返回已上传的分片
			ListPartsRequest listPartsRequest =
					new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
			PartListing partListing = amazonS3.listParts(listPartsRequest);
			result.setFinished(false);
			result.getTaskRecord().setExitPartList(partListing.getParts());
		}
		return result;
	}

	/**
	 * 初始化一个任务
	 */
	public FileTaskDTO initTask(FileTaskVO param) {
		Date currentDate = new Date();
		String bucketName = minioProperties.getBucketName();
		String fileName = param.getFileName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		String key =
				String.format("%s/%s.%s", DateHelper.format(currentDate, "YYYY-MM-dd"), DigestHelper.uuid(), suffix);
		String contentType = MediaTypeFactory.getMediaType(key).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);
		InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(
				new InitiateMultipartUploadRequest(bucketName, key).withObjectMetadata(objectMetadata));
		String uploadId = initiateMultipartUploadResult.getUploadId();

		int chunkNum = (int) Math.ceil(param.getTotalSize() * 1.0 / param.getChunkSize());
		FileTaskEntity task = FileTaskEntity.builder().bucketName(minioProperties.getBucketName()).chunkNum(chunkNum)
				.chunkSize(param.getChunkSize()).totalSize(param.getTotalSize()).fileMd5(param.getMd5())
				.fileName(fileName).objectKey(key).uploadId(uploadId).build();
		fileTaskMapper.insert(task);
		return FileTaskDTO.builder().finished(false).taskRecord(FileTaskRecordDTO.convertFromEntity(task))
				.path(getPath(bucketName, key)).build();
	}

	/**
	 * 生成预签名上传url
	 * 
	 * @param bucket 桶名
	 * @param objectKey 对象的key
	 * @param params 额外的参数
	 * @return
	 */
	public String genPreSignUploadUrl(String bucket, String objectKey, Map<String, String> params) {
		Date expireDate = DateHelper.dateAdd(new Date(), Calendar.MILLISECOND, PRE_SIGN_URL_EXPIRE.intValue());
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectKey)
				.withExpiration(expireDate).withMethod(HttpMethod.PUT);
		if (params != null) {
			params.forEach((key, val) -> request.addRequestParameter(key, val));
		}
		URL preSignedUrl = amazonS3.generatePresignedUrl(request);
		return preSignedUrl.toString();
	}

	/**
	 * 合并分片
	 * 
	 * @param identifier
	 */
	public CompleteMultipartUploadResult merge(String identifier) {
		FileTaskEntity task = getByMd5(identifier);
		if (task == null) {
			throw new RuntimeException("分片任务不存");
		}

		ListPartsRequest listPartsRequest =
				new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
		PartListing partListing = amazonS3.listParts(listPartsRequest);
		List<PartSummary> parts = partListing.getParts();
		if (!task.getChunkNum().equals(parts.size())) {
			// 已上传分块数量与记录中的数量不对应,不能合并分块
			throw new RuntimeException("分片缺失,请重新上传");
		}
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
				.withUploadId(task.getUploadId()).withKey(task.getObjectKey()).withBucketName(task.getBucketName())
				.withPartETags(parts.stream()
						.map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag()))
						.collect(Collectors.toList()));
		return amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
	}

	public String getPath(String bucket, String objectKey) {
		return String.format("%s/%s/%s", minioProperties.getEndpoint(), bucket, objectKey);
	}

	public FileTaskEntity getByMd5(String md5) {
		return fileTaskMapper.getByMd5(md5);
	}
}