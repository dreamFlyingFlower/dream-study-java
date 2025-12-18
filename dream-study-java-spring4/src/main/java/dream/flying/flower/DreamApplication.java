package dream.flying.flower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.service.registry.ImportHttpServices;

import dream.flying.flower.config.BeanRegisterConfig;
import dream.flying.flower.config.WebFluxVersionConfig;
import dream.flying.flower.controller.WebFluxVersionController;

/**
 * SpringBoot4新特性
 * 
 * <pre>
 * 1.API版本控制.在@GetMapping,@PostMapping等注解中添加了version方法,根据版本控制使用的策略,确定版本值的获取方式
 * 	1.1.使用版本控制之后,所有的请求都必须带上该版本字段,系统并不会找默认方法,也许后面的版本会改进这一点
 * 	1.2.代码详见{@link WebFluxVersionConfig},{@link WebFluxVersionController}
 * 
 * 2.新的bean注入方式,但本质上和之前并没有什么不同.详见{@link BeanRegisterConfig}
 * 
 * 3.{@link ImportHttpServices}:更快捷的http代理创建
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-12-18 13:55:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class DreamApplication {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SpringApplication.run(DreamApplication.class, args);
	}
}