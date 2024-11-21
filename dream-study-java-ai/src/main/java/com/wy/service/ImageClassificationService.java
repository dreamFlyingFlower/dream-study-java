package com.wy.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ai.djl.MalformedModelException;
import ai.djl.translate.TranslateException;

/**
 * DJL图片训练接口
 *
 * @author 飞花梦影
 * @date 2024-11-21 14:13:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ImageClassificationService {

	String predict(MultipartFile image, String modePath)
			throws IOException, MalformedModelException, TranslateException;

	String training(String datasetRoot, String modePath) throws TranslateException, IOException;
}