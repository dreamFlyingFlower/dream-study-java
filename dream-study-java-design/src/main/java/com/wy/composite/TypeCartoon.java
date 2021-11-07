package com.wy.composite;

import java.util.List;

import com.wy.entity.Cartoon;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-11-05 20:36:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface TypeCartoon extends Cartoon {

	List<TypeCartoon> children();
}