package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;
import com.wy.service.MyAsyncService01;

/**
 * 测试API
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("testAsync")
public class TestAsyncCrl {

	@Autowired
	private MyAsyncService01 myAsyncService01;

	@GetMapping("test01")
	public Result<?> test01() {
		myAsyncService01.test1();
		return Result.ok();
	}

	@GetMapping("test02")
	public Result<?> test02() {
		myAsyncService01.test2();
		return Result.ok();
	}
}