package ar.com.ip.trace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * @author Leandro Coello (leandro.n.coello@gmail.com)
 *
 */
@SpringBootApplication(scanBasePackages = {"ar.com.ip.trace"})
@EnableAutoConfiguration
public class IPTraceBackendApplication extends SpringBootServletInitializer{
		
	public static void main(String[] args) {
		SpringApplication.run(IPTraceBackendApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IPTraceBackendApplication.class);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}