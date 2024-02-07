package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
		 	
		 	//les request à autorisé
		 	.authorizeHttpRequests(
		 			requests-> requests.requestMatchers("/login").permitAll() //autorise tout le monde à acceder a cet url
		 						.anyRequest().authenticated() //pour tout les autre : doit etre authentifié
		 	) ;
		 
		//construire un objet httpsecurity et retourner
		return http.build();
	}

}
