package com.wy.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.wy.result.ResultException;

/**
 * 根据hibernate的注解检查实例对象是否正常
 * 需配合hibernate,要在实体类上添加相应的注解org.hibernate.validator.constraints.*
 * @author paradiseWy
 */
public class ValidationUtils {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * 检查整个实体类是否验证通过,需要配合hibernate注解
	 * @param t 实例对象
	 */
	public static <T> void validateEntity(T t) {
		Set<ConstraintViolation<T>> sets = validator.validate(t, Default.class);
		if (sets != null && !sets.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			for (ConstraintViolation<T> constraint : sets) {
				msg.append(constraint.getMessage()).append("<br>");
			}
			throw new ResultException(msg.toString());
		}
	}

	/**
	 * 检查实例对象中某个字段的值
	 * @param obj 实例对象
	 * @param propertyName 属性名
	 */
	public static <T> void validateProperty(T obj, String propertyName) {
		Set<ConstraintViolation<T>> sets = validator.validateProperty(obj, propertyName,
				Default.class);
		if (sets != null && !sets.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			for (ConstraintViolation<T> constraint : sets) {
				msg.append(constraint.getMessage()).append("<br>");
			}
			throw new ResultException(msg.toString());
		}
	}
}