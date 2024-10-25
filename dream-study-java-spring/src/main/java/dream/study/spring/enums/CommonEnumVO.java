package dream.study.spring.enums;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:34:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(description = "通用枚举")
public class CommonEnumVO {

	@ApiModelProperty(notes = "Code")
	private final Integer code;

	@ApiModelProperty(notes = "Name")
	private final String name;

	@ApiModelProperty(notes = "描述")
	private final String msg;

	public static CommonEnumVO from(CommonEnum commonEnum) {
		if (commonEnum == null) {
			return null;
		}
		return new CommonEnumVO(commonEnum.getCode(), commonEnum.getName(), commonEnum.getMsg());
	}

	public static List<CommonEnumVO> from(List<CommonEnum> commonEnums) {
		if (CollectionUtils.isEmpty(commonEnums)) {
			return Collections.emptyList();
		}
		return commonEnums.stream().filter(Objects::nonNull).map(CommonEnumVO::from).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}