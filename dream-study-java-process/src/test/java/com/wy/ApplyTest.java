package com.wy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wy.model.Apply;
import com.wy.model.Resume;
import com.wy.service.ApplyService;
import com.wy.service.ResumeService;

@SpringBootTest
public class ApplyTest {

	@Autowired
	private ApplyService service;

	@Autowired
	ResumeService resumeService;

	@Test
	public void load() {
		Apply apply = service.getById("95b217a2-72aa-4973-8124-8c3c131ace40");
		System.out.println(apply.getResumes().size());
	}

	@Test
	public void load1() {
		List<Resume> resumes = resumeService.findResumesByApplyId("95b217a2-72aa-4973-8124-8c3c131ace40");
		System.out.println(resumes.size());
	}
}