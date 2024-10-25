package dream.study.spring.minio.upload;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.amazonaws.services.s3.model.PartSummary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:41:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileTaskRecordDTO extends FileTaskEntity {

	private static final long serialVersionUID = -8192608325154934418L;

	/**
	 * 已上传完的分片
	 */
	private List<PartSummary> exitPartList;

	public static FileTaskRecordDTO convertFromEntity(FileTaskEntity task) {
		FileTaskRecordDTO dto = new FileTaskRecordDTO();
		BeanUtils.copyProperties(task, dto);
		return dto;
	}
}