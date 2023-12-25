package craft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"craft"})
public class OrdersConfig {
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver vr=new InternalResourceViewResolver();
		//vr.setPrefix("/view");
		vr.setSuffix(".jsp");
		return vr;
	}
}
