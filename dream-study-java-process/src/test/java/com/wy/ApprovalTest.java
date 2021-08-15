package com.wy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wy.service.ApprovalService;

@SpringBootTest
public class ApprovalTest {

	@Autowired
	private ApprovalService approvalService;

	@Test
	public void listResume() {
		System.out.println(approvalService.listResume("3"));
	}
}