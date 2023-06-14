package com.wy.service.impl;

import org.springframework.stereotype.Service;

import com.wy.model.SysLog;
import com.wy.service.SysLogService;

/**
 * 系统日志业务实现类
 *
 * @author 飞花梦影
 * @date 2021-12-09 17:02:13
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
public class SysLogServiceImpl implements SysLogService {

	public int age = 10;

	@Override
	public void create(SysLog sysLog) {

	}

	public int test() {
		return 0;
	}

	public int test(int i) {
		return i;
	}
}