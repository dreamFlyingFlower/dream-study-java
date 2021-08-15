package com.wy.controller;

import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ProcessEngine;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("initAction")
@Scope("prototype")
public class InitCrl {

	@Resource
	private ProcessEngine processEngine;

	public String init(HttpServletRequest request) throws Exception {
		String path = request.getServletContext().getRealPath("/WEB-INF/classes/jbpm/task.zip");
		ZipInputStream zis = new ZipInputStream(new FileInputStream(path));
		// processEngine.getRepositoryService().createDeployment().addResourcesFromZipInputStream(zis).deploy();
		processEngine.getRepositoryService().createDeployment().addZipInputStream(zis).deploy();
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("customerId", "001");
		 * processEngine.getExecutionService().startProcessInstanceByKey("task", map);
		 */
		return "init";
	}

	public String end() {
		processEngine.getRuntimeService().deleteProcessInstance("itheimaTask.9", "删除");
		// processEngine.getRuntimeService().endProcessInstance("itheimaTask.9");
		return "init";
	}
}