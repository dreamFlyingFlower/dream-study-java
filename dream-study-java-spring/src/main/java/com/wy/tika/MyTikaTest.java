package com.wy.tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-03-25 13:44:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTikaTest {

	@Autowired
	private Tika tika;

	public void test() throws IOException, TikaException {
		Path path = Paths.get("D:", "test.pdf");
		File file = path.toFile();

		String text = tika.parseToString(file);
		System.out.println(text);
	}
}