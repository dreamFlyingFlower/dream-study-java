package com.wy.tika;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.SAXException;

/**
 * Apache tika是Apache开源的一个文档解析工具,可以解析和提取一千多种不同的文件类型(如PPT、XLS和PDF)的内容和格式.
 * 
 * 可以使用图形化操作页面(tika-app),又可以独立部署(tika-server)通过接口调用,还可以引入到项目中使用
 *
 * @author 飞花梦影
 * @date 2024-03-25 13:40:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyTikaConfig {

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public Tika tika() throws TikaException, IOException, SAXException {
		// 配置文件
		Resource resource = resourceLoader.getResource("classpath:tika.xml");
		InputStream inputStream = resource.getInputStream();

		TikaConfig config = new TikaConfig(inputStream);
		Detector detector = config.getDetector();
		Parser autoDetectParser = new AutoDetectParser(config);

		return new Tika(detector, autoDetectParser);
	}
}