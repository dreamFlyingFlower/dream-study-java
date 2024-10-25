package dream.study.spring.minio.upload;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:31:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface FileTaskMapper {

	FileTaskEntity getByMd5(String md5);

	int insert(FileTaskEntity task);
}