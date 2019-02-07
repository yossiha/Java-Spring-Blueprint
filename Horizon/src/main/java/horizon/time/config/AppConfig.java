package horizon.time.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.modelmapper.ModelMapper;
import horizon.time.components.StorageComponent;

@Configuration
public class AppConfig {
	@Autowired
	StorageComponent storageComponent;

	@Bean
	void StorageComponentConfig() {
		storageComponent.init();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean(name = "filterMultipartResolver")
	public CommonsMultipartResolver getMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}

}
