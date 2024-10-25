package dream.study.spring.enums;

/**
 * JPA使用例子,同时需要在需要转换的字段上添加@Convert(converter=JpaFileTypeAttributeConverter.class)
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:42:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JpaFileTypeAttributeConverter extends CommonEnumAttributeConverter<FileType> {

	public JpaFileTypeAttributeConverter() {
		super(FileType.values());
	}
}