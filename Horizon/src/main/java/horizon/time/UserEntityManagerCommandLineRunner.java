package horizon.time;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserEntityManagerCommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) {
		System.out.println("-----------------------------------");
		System.out.println("--=> Initializing Application  <=--");
		System.out.println("-----------------------------------");
	}
}
