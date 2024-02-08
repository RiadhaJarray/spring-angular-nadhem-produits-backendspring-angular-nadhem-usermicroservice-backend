package com.example.demo.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//ici nous avons utiliser l'insatnce de "AuthenticationManager" declare deja et creer dans la class "SecurityBeansConfiguration"
	//public AuthenticationManager authenticationManager(AuthenticationConfiguration config) : retourne deja un beans de type  "AuthenticationManager"
	@Autowired
	AuthenticationManager authMgr;

	
	//spring security composé de plusieurs filters (chane de filter) :donc on est besion d'un filter 
	//
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
	 
		//session management confihuration
		//on va dire a spring de ne pas enregistrer les sessions utilisateur (not statefull : it is stateless) 
		//je n'ai pas besion de gere les session : just generate un token a partir d'un username et un mot de passe
		 http
		 	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		 
		 	.csrf(csrf -> csrf.disable())//car c'est stateless :je n'ai pas besion de csrf 
		 	
		 	//nous avons ajouter cet partie car les api fonctionne avec postman mais non avec angular
		 	.cors(cors -> cors.configurationSource(new CorsConfigurationSource() 
			 	{
				 	 @Override
				 	 public CorsConfiguration getCorsConfiguration(HttpServletRequest request) 
				 	 {
					 	CorsConfiguration cors = new CorsConfiguration();
					 	 
					 	cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
					 	//si j'ai plus qu'un site a authorisé
					 	//cors.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:5200"));
					 	cors.setAllowedMethods(Collections.singletonList("*"));
					 	cors.setAllowedHeaders(Collections.singletonList("*"));
					 	cors.setExposedHeaders(Collections.singletonList("Authorization"));
					 	 return cors;
				 	 }
			 	 }
		 	))
		 	
		 	//les request à autorisé
		 	.authorizeHttpRequests(
		 			requests-> requests.requestMatchers("/login").permitAll() //autorise tout le monde à acceder a cet url
		 								.requestMatchers("/all").hasAuthority("ADMIN")	
		 								.anyRequest().authenticated() ) //pour tout les autre : doit etre authentifié
		 	
		 	
		 	//filter pour ajouter et generer le JWT 
		 	//"new JWTAuthenticationFilter (authMgr)" : pour faire filter avec JWT
		 	//ensuite avec "UsernamePasswordAuthenticationFilter.class"
		 	.addFilterBefore(new JWTAuthenticationFilter (authMgr), UsernamePasswordAuthenticationFilter.class)
		 	
		 	//filter recuper le jwt et decoder et lire le user et ses roles
		 	.addFilterBefore(new JWTAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
		 
		//construire un objet httpsecurity et retourner
		return http.build();
	}

}
