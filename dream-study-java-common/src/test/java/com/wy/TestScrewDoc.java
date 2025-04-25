package com.wy;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

/**
 * 使用screw生成数据库文档
 *
 * @author 飞花梦影
 * @date 2024-10-08 16:51:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootTest
class TestScrewDoc {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {

		ProcessConfig processConfig = ProcessConfig.builder()
				// 根据名称指定表生成
				.designatedTableName(null)
				// 根据表前缀生成表
				.designatedTablePrefix(null)
				// 根据表后缀生成表
				.designatedTableSuffix(null)
				// 忽略表名
				.ignoreTableName(null)
				// 忽略前缀
				.ignoreTablePrefix(null)
				// 忽略后缀
				.ignoreTableSuffix(null)
				.build();

		EngineConfig engineConfig = EngineConfig.builder()
				// 生成的文件目录路径
				.fileOutputDir(null)
				.openOutputDir(false)
				// 生成的文件类型
				.fileType(EngineFileType.MD)
				// 模板实现,根据事情情况选择
				.produceType(EngineTemplateType.freemarker)
				.build();

		Configuration configuration = Configuration.builder()
				.version(null)
				.description(null)
				.dataSource(applicationContext.getBean(DataSource.class))
				.engineConfig(engineConfig)
				.produceConfig(processConfig)
				.build();

		// 执行文件生成
		new DocumentationExecute(configuration).execute();
	}
}