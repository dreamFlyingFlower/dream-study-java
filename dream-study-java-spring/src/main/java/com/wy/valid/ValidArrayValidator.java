package com.wy.valid;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

/**
 * 对自定义校验规则的注解进行处理,此处处理@ValidArray
 * 
 * {@link ConstraintValidator}:第一个参数为需要检验的注解,第二个参数为正在校验的字段类型
 * 
 * 可用的校验注解:
 * 
 * <pre>
 * {@link AssertTrue}:用于boolean字段,该字段只能为true
 * {@link AssertFalse}:用于boolean字段,该字段只能为false
 * {@link DecimalMax}:只能小于或等于该值
 * {@link DecimalMin}:只能大于或等于该值
 * {@link Digits}:只能是一个数,包括正负数,小数
 * {@link Email}:检查是否是一个有效的email地址
 * {@link Future}:检查该字段的日期是否是属于将来的日期
 * {@link FutureOrPresent}:检查该字字段的日期是否是属于将来或当前日期
 * {@link Max}:该字段的值只能小于或等于该值
 * {@link Min}:该字段的值只能大于或等于该值
 * {@link Negative}:检查是否为一个负数,包括小数
 * {@link NegativeOrZero}:检查是否为一个小等于0的负数,包括小数
 * {@link NotBlank}:不能为空,检查时会将空格忽略
 * {@link NotEmpty}:不能为空,这里的空是对象为不null,但是对象中没有元素,可用于字符串,数组,集合,Map
 * {@link NotNull}:不能为null
 * {@link Null}:必须为null
 * {@link Past}:检查字段的日期是否是过去的日期
 * {@link PastOrPresent}:检查字段的日期是否是过去或当前日期
 * {@link Pattern}:被注释的元素必须符合指定的正则表达式
 * {@link Positive}:检查字段是否为一个正数,包括小数
 * {@link PositiveOrZero}:检查字段是否为一个大于等于0的正数
 * {@link Size}:检查所属的字段的长度是否在min和max之间,可用于字符串,数组,集合,Map
 * 
 * {@link CreditCardNumber}:对信用卡号进行一个大致的验证
 * {@link URL}:检查是否是一个有效的URL,如果提供了protocol,host等,则该URL还需满足提供的条件
 * 
 * {@link Valid}: 当需要进行嵌套检验时,即实体类中需要检验另外的实体类时可使用该注解
 * </pre>
 *
 * @author 飞花梦影
 * @date 2020-12-19 10:11:43
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class ValidArrayValidator implements ConstraintValidator<ValidArray, Integer> {

	private Set<Integer> set = new HashSet<>();

	/**
	 * 初始化方法,从上下文中拿到自定义注解
	 * 
	 * @param validArray 自定义注解
	 */
	@Override
	public void initialize(ValidArray validArray) {
		int[] vals = validArray.vals();
		for (int val : vals) {
			set.add(val);
		}
	}

	/**
	 * 判断是否校验成功
	 * 
	 * @param value 需要校验的值
	 * @param context 上下文环境
	 * @return true成功,false失败
	 */
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return set.contains(value);
	}
}