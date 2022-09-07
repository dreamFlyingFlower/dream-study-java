package com.wy.crl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.wy.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 测试WebFlux的用户API接口
 * 
 * @author 飞花梦影
 * @date 2020-11-23 14:16:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserCrl {

	@Autowired
	private UserRepository userRepository;

	/**
	 * 使用WebClient调用其他的WebFlux接口,不能本项目的调用本项目的,会抛阻塞异常
	 */
	@GetMapping("testWebClient")
	public void testWebClient() {
		Mono<String> user = WebClient.create().get().uri("http://localhost:8080/user/testMono")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);
		System.out.println(user.block());
	}

	@GetMapping("/common")
	public String commonHandle() {
		log.info("common--start");
		// 同步执行耗时操作,阻塞
		String result = doSome("normal use");
		log.info("common--end");
		return result;
	}

	@GetMapping("/mono")
	public Mono<String> monoHandle() {
		log.info("mono--start");
		// 异步执行耗时操作,但也要等到整个方法执行完毕才返回结果
		Mono<String> mono = Mono.fromSupplier(() -> doSome("webflux use"));
		log.info("mono--end");
		// 返回的mono可以直接在前端进行解析
		return mono;
	}

	/**
	 * 定义耗时操作,非线程操作
	 */
	private String doSome(String msg) {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 如果一个Stream无返回值,但是又必须返回一个值,可以在操作后接then,返回一个值
	 * 
	 * @param id
	 * @return
	 */
	public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
		return userRepository.findById(id)
				.flatMap(t -> userRepository.delete(t).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}