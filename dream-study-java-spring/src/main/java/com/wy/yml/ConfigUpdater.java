package com.wy.yml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * 动态给bootstrap.yml添加配置
 *
 * @author 飞花梦影
 * @date 2024-05-08 17:59:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class ConfigUpdater {

	public void updateLoadJars(List<String> jarNames) throws IOException {
		// 读取bootstrap.yml
		Yaml yaml = new Yaml();
		InputStream inputStream = new FileInputStream(new File("src/main/resources/bootstrap.yml"));
		Map<String, Object> obj = yaml.load(inputStream);
		inputStream.close();

		obj.put("loadjars", jarNames);

		try (FileWriter writer = new FileWriter(new File("src/main/resources/bootstrap.yml"))) {
			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);
			Yaml yamlWriter = new Yaml(options);
			yamlWriter.dump(obj, writer);
		}
	}
}