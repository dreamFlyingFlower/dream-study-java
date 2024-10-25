package dream.study.spring.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Minio配置文件
 *
 * @author 飞花梦影
 * @date 2023-06-20 15:48:34
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
@ConfigurationProperties("config.minio")
@Data
public class MinioProperties {

	/** minio服务地址 */
	private String endpoint;

	/** 文件服务域名 */
	private String fileHost;

	/** 桶名称 */
	private String bucketName;

	/** minio服务登录用户名 */
	private String accessKey;

	/** minio服务登录密码 */
	private String secretKey;
}