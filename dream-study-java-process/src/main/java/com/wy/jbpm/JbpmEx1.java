//package com.wy.jbpm;
//
//import org.hibernate.cfg.Configuration;
//
///**
// * jbpm的eclipse插件安装,在jbpm压缩包install/src/gpd,通过hibernate创建jbpm所需的18张表
// *
// * @author ParadiseWY
// * @date 2018-03-14 23:36:21
// * @git {@link https://github.com/mygodness100}
// */
//public class JbpmEx1 {
//
//	public static void main(String[] args) {}
//
//	public static void createTable() {
//		// 先要通过hibernate在数据库中创建18张表,注意此处的configuration是hibernate的,不是jbpm的
//		Configuration config = new Configuration();
//		config.configure("jbpm.hibernate.cfg.xml");
//		config.buildSessionFactory();
//	}
//}