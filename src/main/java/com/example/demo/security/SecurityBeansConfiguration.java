package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//on ajoute cette annotation car notre class va : 
//contenir des beans 
//des singlentons que vont etre creer par spring a etuliser par d'autre class dans notre cas des class de security
//un best practice est de mettre tout mes beans (singleton) dans un meme class 
@Configuration
public class SecurityBeansConfiguration {
	
	 @Bean
	 public BCryptPasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	 }
	 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception 
	{
		return config.getAuthenticationManager();
	}
}
