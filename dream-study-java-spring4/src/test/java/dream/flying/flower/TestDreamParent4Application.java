package dream.flying.flower;

import org.springframework.boot.SpringApplication;

public class TestDreamParent4Application {

	public static void main(String[] args) {
		SpringApplication.from(DreamParent4Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
