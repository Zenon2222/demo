package com.example.demo;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext  context = SpringApplication.run(DemoApplication.class, args);
		ConfigurableListableBeanFactory beansFactory = context.getBeanFactory();

        //ConfigurableApplicationContext contextApp
        //= new AnnotationConfigApplicationContext(SecurityFilterChain.class);
        SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
        FilterChainProxy proxy = context.getBean(FilterChainProxy.class);
        HttpSecurity http = context.getBean(HttpSecurity.class);

		System.out.println("bean="+chain.toString());
		System.out.println("proxy="+proxy.toString());
        
		
		String[] list =beansFactory.getBeanDefinitionNames();
		
		for (String s : list) {
			BeanDefinition def =beansFactory.getBeanDefinition(s);
			String defString = def.toString();
			String className = def.getBeanClassName();
			//System.out.println(s+ " ,bean="+defString);
			//System.out.println(className);
			
		}
		
		
	}
	
	
	@Configuration
	public class SecurityConfiguration {

	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    	http
            .exceptionHandling()
            .accessDeniedHandler((req, res, e) -> res.sendRedirect("/403"))

    .and().authorizeHttpRequests()		//AuthorizationManagerRequestMatcherRegistry
            .requestMatchers(HttpMethod.GET,"/", "/custom-login", "/css/**").permitAll()
            .anyRequest().authenticated()

     // send the user back to the root page when they logout
    .and()
        .logout().logoutSuccessUrl("/");

	    	
	    
	    
	        return http.build();
	    }

	}
	

}
