package dream.flying.flower.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2025-12-18 14:12:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("test")
public class WebFluxVersionController {

	@GetMapping(value = "test1", version = "")
	public void test1() {
		System.out.println("test1.1");
	}

	@GetMapping(value = "test1", version = "2")
	public void test2() {
		System.out.println("test1.2");
	}
}