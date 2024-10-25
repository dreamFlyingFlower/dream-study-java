package dream.study.spring.enums;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

/**
 * 参数转化扩展
 * 
 * <pre>
 * 1.对于普通参数,比如 RequestParam 或 PathVariable 直接从 ConditionalGenericConverter 进行扩展
 * 基于 CommonEnumRegistry 提供的 CommonEnum 信息,对 matches 和 getConvertibleTypes方法进行重写
 * 根据目标类型获取所有的枚举值,并根据 code 和 name 进行转化
 * 2.对于 Json 参数,需要对 Json 框架进行扩展（以 Jackson 为例）
 * 遍历 CommonEnumRegistry 提供的所有 CommonEnum,依次进行注册
 * 从 Json 中读取信息,根据 code 和 name 转化为确定的枚举值
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:38:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Order(1)
@Component
public class CommonEnumConverter implements ConditionalGenericConverter {

	@Autowired
	private CommonEnumRegistry enumRegistry;

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		Class<?> type = targetType.getType();
		return enumRegistry.getClassDict().containsKey(type);
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return enumRegistry.getClassDict().keySet().stream().map(cls -> new ConvertiblePair(String.class, cls))
				.collect(Collectors.toSet());
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String value = (String) source;
		List<CommonEnum> commonEnums = this.enumRegistry.getClassDict().get(targetType.getType());
		return commonEnums.stream().filter(commonEnum -> commonEnum.match(value)).findFirst().orElse(null);
	}
}