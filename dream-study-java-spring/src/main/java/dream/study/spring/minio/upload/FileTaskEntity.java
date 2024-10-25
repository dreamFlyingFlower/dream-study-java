package dream.study.spring.minio.upload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 文件上传任务数据库映射类
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:16:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileTaskEntity implements Serializable {

	private static final long serialVersionUID = -6181409311271844249L;

	private Long id;

	/**
	 * 分片上传的uploadld
	 */
	private String uploadId;

	/**
	 * 文件MD5值
	 */
	private String fileMd5;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 所属桶名
	 */
	private String bucketName;

	/**
	 * 文件的key
	 */
	private String objectKey;

	/**
	 * 文件大小(byte)
	 */
	private Long totalSize;

	/**
	 * 每个分片大小(byte)
	 */
	private Long chunkSize;

	/**
	 * 分片数量
	 */
	private Integer chunkNum;
	
	/**
	 * 状态:1上传中;2上传完成;3上传失败
	 */
	private Integer status;
}