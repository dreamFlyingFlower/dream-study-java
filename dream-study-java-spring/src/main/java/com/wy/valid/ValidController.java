package com.wy.valid;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.wy.model.User;

import lombok.AllArgsConstructor;

/**
 * 校验
 *
 * @author 飞花梦影
 * @date 2024-07-09 09:48:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@AllArgsConstructor
public class ValidController {

	// 全局校验
	private final Validator validator;

	public void test() {
		User user = new User();
		Set<ConstraintViolation<User>> validate = validator.validate(user, User.class);
		if (validate.isEmpty()) {
			// 如果校验通过,isEmpty为true
		} else {
			// 如果校验不通过
			for (ConstraintViolation<User> constraintViolation : validate) {
				System.out.println(constraintViolation.getMessage());
			}
		}
	}

	/**
	 * 结合Spring,当前方法校验失败后,Spring会将结果放到BindingResult中,可直接从该参数中获得异常信息
	 * 
	 * 也可以拦截全局异常{@link MethodArgumentNotValidException}和{@link ConstraintViolationException}进行处理
	 * 
	 * @param user 需要进行校验的对象
	 * @param bindingResult 校验结果
	 */
	public void test1(@Validated User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError error = bindingResult.getFieldError();
			System.out.println(error.getField() + error.getDefaultMessage());
		}
	}
}