package horizon.time;

import java.lang.invoke.MethodHandles;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@EnableAutoConfiguration // included in @SpringBootApplication
@ComponentScan("horizon.time") // scan and bootstrap other components defined in the current package
@EnableJpaRepositories("horizon.time.persistence.repo")
@EntityScan("horizon.time.*")
@Configuration
@SpringBootApplication
public class HorizonApplication {
	final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		logger.info("Initializing application, ..");

		SpringApplication.run(HorizonApplication.class, args);
	}
}
