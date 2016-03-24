package com.framework.core.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:config/spring.xml")
public class Application extends SpringBootServletInitializer{
    public static void main(String[] args) {
    	System.out.println("*****************begin*******************");
        SpringApplication.run(Application.class);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
//	 @Bean
//	 public FilterRegistrationBean sitemeshFilter(){
//	     FilterRegistrationBean filter = new FilterRegistrationBean();
//	     filter.setName("sitemesh");
//	     filter.setFilter(new ConfigurableSiteMeshFilter());
//	     filter.addUrlPatterns("/*");
////	     filter.setEnabled(false);
//	     
//	     System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
////	     filter.addInitParameter("decorators-file", "/WEB-INF/deco222rators.xml");
////	     filter.addInitParameter("contextConfigLocation", "classpath:/WEB-INF/sitemesh.xml");
//	     return filter;
//	 }
}
