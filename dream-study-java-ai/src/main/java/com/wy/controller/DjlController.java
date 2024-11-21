package com.wy.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wy.service.ImageClassificationService;

import ai.djl.MalformedModelException;
import ai.djl.translate.TranslateException;
import lombok.AllArgsConstructor;

/**
 * DJL图形训练
 * 
 * @author 飞花梦影
 * @date 2021-11-23 21:14:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("djl")
@AllArgsConstructor
public class DjlController {

	private final ImageClassificationService imageClassificationService;

	@PostMapping(path = "/analyze")
	public String predict(@RequestPart("image") MultipartFile image,
			@RequestParam(defaultValue = "/home/djl-test/models") String modePath)
			throws TranslateException, MalformedModelException, IOException {
		return imageClassificationService.predict(image, modePath);
	}

	@PostMapping(path = "/training")
	public String training(@RequestParam(defaultValue = "/home/djl-test/images-test") String datasetRoot,
			@RequestParam(defaultValue = "/home/djl-test/models") String modePath)
			throws TranslateException, IOException {
		return imageClassificationService.training(datasetRoot, modePath);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource>
			downloadFile(@RequestParam(defaultValue = "/home/djl-test/images-test") String directoryPath) {
		List<String> imgPathList = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
			// Filter only regular files (excluding directories)
			paths.filter(Files::isRegularFile).forEach(c -> imgPathList.add(c.toString()));
		} catch (IOException e) {
			return ResponseEntity.status(500).build();
		}
		Random random = new Random();
		String filePath = imgPathList.get(random.nextInt(imgPathList.size()));
		Path file = Paths.get(filePath);
		Resource resource = new FileSystemResource(file.toFile());
		if (!resource.exists()) {
			return ResponseEntity.notFound().build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName().toString());
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
		try {
			return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(500).build();
		}
	}
}