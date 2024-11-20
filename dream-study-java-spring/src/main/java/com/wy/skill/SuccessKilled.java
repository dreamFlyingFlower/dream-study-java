package com.wy.skill;

import java.io.Serializable;

import lombok.Data;

/**
 * 商品秒杀对象.如不出现问题,需使用AOP将整个Service业务层代码,使用 MethodLock 注解
 *
 * @author 飞花梦影
 * @date 2024-05-29 10:42:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class SuccessKilled implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long seckillId;

	private Long userId;
}