package com.wy.shiro.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Objects;

import com.wy.lang.StrTool;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义序列化工具
 * 
 * @author 飞花梦影
 * @date 2022-06-22 09:33:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class ShiroRedissionSerialize {

	/**
	 * 序列化方法
	 * 
	 * @param object
	 * @return
	 */
	public static String serialize(Object object) {
		if (Objects.isNull(object)) {
			return null;
		}
		String encodeBase64 = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(bos);) {
			oos.writeObject(object);
			encodeBase64 = Base64.getEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("流写入异常:{}", e);
		}
		return encodeBase64;
	}

	/**
	 * 反序列化方法
	 * 
	 * @param str
	 * @return
	 */
	public static Object deserialize(String str) {
		if (StrTool.isBlank(str)) {
			return null;
		}
		Object object = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(str));
		        ObjectInputStream ois = new ObjectInputStream(bis);) {
			object = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("流读取异常:{}", e);
		}
		return object;
	}
}