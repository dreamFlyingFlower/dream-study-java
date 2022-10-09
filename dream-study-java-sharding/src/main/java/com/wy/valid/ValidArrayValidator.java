package com.wy.valid;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 对自定义校验规则的注解进行处理,此处处理@ValidArray
 *
 * @author ParadiseWY
 * @date 2020-12-19 10:11:43
 * @git {@link https://github.com/mygodness100}
 */
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