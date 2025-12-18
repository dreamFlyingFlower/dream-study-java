package dream.flying.flower;

import org.springframework.boot.SpringApplication;

public class TestDreamApplication {

	public static void main(String[] args) {
		SpringApplication.from(DreamApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
