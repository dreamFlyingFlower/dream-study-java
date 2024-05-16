package com.wy.generator;

import java.util.Scanner;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.dream.lang.StrHelper;

/**
 * MyBatisPlus代码生成器
 * 
 * @author 飞花梦影
 * @date 2022-05-09 14:02:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyBatisPlusGenerator {

	/**
	 * 读取控制台内容
	 */
	public static String scanner(String tip) {
		Scanner scanner = new Scanner(System.in);
		try {
			StringBuilder help = new StringBuilder();
			help.append("请输入" + tip + "：");
			System.out.println(help.toString());
			if (scanner.hasNext()) {
				String ipt = scanner.next();
				if (StrHelper.isNotBlank(ipt)) {
					return ipt;
				}
			}
		} finally {
			scanner.close();
		}
		return null;
	}

	public static void main(String[] args) {
		// 数据源配置 需配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("url", "uername", "password").build();
		// 代码生成器
		AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig);

		String projectPath = System.getProperty("user.dir");
		// 当前项目名
		String projectName = "/generator";
		// 全局配置
		GlobalConfig globalConfig = new GlobalConfig.Builder().outputDir(projectPath + projectName + "/src/main/java")
				.author("飞花梦影").build();
		autoGenerator.global(globalConfig);

		// 生成包配置
		PackageConfig packageConfig = new PackageConfig.Builder().parent("com.wy").moduleName(scanner("模块名")).build();
		autoGenerator.packageInfo(packageConfig);

		// 配置模板,注意不要带上.ftl/.vm
		TemplateConfig templateConfig = new TemplateConfig.Builder().entity("templates/entity.java")
				.mapper("templates/mapper.xml").service("templates/service.java")
				.serviceImpl("templates/serviceImpl.java").controller("templates/controller.java").build();

		autoGenerator.template(templateConfig);

		// 策略配置
		StrategyConfig strategyConfig = new StrategyConfig.Builder()
				// 若只需要生成单个表,可直接指定表名
				// .addInclude("")
				// 表名映射到实体名称去掉前缀
				.addTableSuffix(packageConfig.getModuleName() + "_").mapperBuilder().entityBuilder()
				// 设置父类
				.superClass("com.wy.base.BaseEntity")
				// 表名映射到实体策略,带下划线的转成驼峰
				.naming(NamingStrategy.underline_to_camel)
				// 列名映射到类型属性策略,带下划线的转成驼峰
				.columnNaming(NamingStrategy.underline_to_camel).enableLombok().controllerBuilder().enableRestStyle()
				.superClass("com.wy.base.AbstractController").build();

		autoGenerator.strategy(strategyConfig);
		System.out.println("===================== MyBatis Plus Generator ==================");
		autoGenerator.execute();
		System.out.println("================= MyBatis Plus Generator Execute Complete ==================");
	}
}