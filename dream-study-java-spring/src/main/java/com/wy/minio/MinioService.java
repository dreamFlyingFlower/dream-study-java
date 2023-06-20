package com.wy.minio;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.messages.Item;
import lombok.SneakyThrows;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-06-20 16:00:47
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
public class MinioService {

	@Autowired
	private MinioClient minioClient;

	@SneakyThrows
	public void test(MultipartFile multipartFile) {
		// 检查bucket块是否存在,类似于命名空间
		boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("bucket").build());
		if (!found) {
			// 新建一个块bucket
			minioClient.makeBucket(MakeBucketArgs.builder().bucket("bucket").build());
		} else {
			System.out.println("Bucket 'bucket' already exists.");
		}

		// 查询所有的bucket
		System.out.println(minioClient.listBuckets());
		// 根据条件查询bucket中符合条件的信息,见该方法的例子
		Iterator<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder().build()).iterator();
		while (listObjects.hasNext()) {
			System.out.println(listObjects.next());
		}

		// 上传本地/home/user/Photos/test.zip作为test.zip到块bucket中
		minioClient.uploadObject(UploadObjectArgs.builder().bucket("bucket")
				// 上传后的文件名
				.object("test.zip")
				// 本地需要上传的文件地址
				.filename("/home/user/Photos/test.zip").build());
		// 从流中读取文件
		minioClient.putObject(PutObjectArgs.builder().bucket("bucket").object("test.zip")
				// 上传的流
				.stream(multipartFile.getInputStream(), multipartFile.getSize(), -1).build());
	}
}