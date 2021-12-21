package com.xxl.job.admin.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.xxl.job.admin.core.model.XxlJobGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobGroupDaoTest {

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;

	@Test
	public void test() {
		XxlJobGroup group = new XxlJobGroup();
		group.setAppname("setAppName");
		group.setTitle("setTitle");
		group.setAddressType(0);
		group.setAddressList("setAddressList");
		group.setUpdateTime(new Date());
		xxlJobGroupDao.save(group);
		XxlJobGroup group2 = xxlJobGroupDao.load(group.getId());
		group2.setAppname("setAppName2");
		group2.setTitle("setTitle2");
		group2.setAddressType(2);
		group2.setAddressList("setAddressList2");
		group2.setUpdateTime(new Date());
		xxlJobGroupDao.update(group2);
		xxlJobGroupDao.remove(group.getId());
	}
}