package com.xxl.job.admin.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.xxl.job.admin.core.model.XxlJobLogGlue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobLogGlueDaoTest {

	@Resource
	private XxlJobLogGlueDao xxlJobLogGlueDao;

	@Test
	public void test() {
		XxlJobLogGlue logGlue = new XxlJobLogGlue();
		logGlue.setJobId(1);
		logGlue.setGlueType("1");
		logGlue.setGlueSource("1");
		logGlue.setGlueRemark("1");
		logGlue.setAddTime(new Date());
		logGlue.setUpdateTime(new Date());
		xxlJobLogGlueDao.save(logGlue);
		xxlJobLogGlueDao.findByJobId(1);
		xxlJobLogGlueDao.removeOld(1, 1);
		xxlJobLogGlueDao.deleteByJobId(1);
	}
}