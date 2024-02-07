package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UsersMicroserviceApplication {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}
	
	// cette methode va etre executer just apres l'execution du constructeur de la classe suivante: 
	//dans notre cas elle va etre executer apres toute redemarrage de l'application
	/*@PostConstruct
	void init_users() {
		
		//ajouter les rôles
		userService.addRole(new Role(null,"ADMIN"));
		userService.addRole(new Role(null,"USER"));
		
		//ajouter les users
		userService.saveUser(new User(null,"admin","123",true,null));
		userService.saveUser(new User(null,"riadh","123",true,null));
		userService.saveUser(new User(null,"yassine","123",true,null));
		
		//ajouter les rôles aux users
		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		userService.addRoleToUser("riadh", "USER");
		userService.addRoleToUser("yassine", "USER");
	} */
	
	
	//un singleton (instance) : qui sera disponible pour tout le projet
	//@Autowired BCryptPasswordEncoder bCryptPasswordEncoder; ==> a chaque appel comme cele ci; spring va appeler cette instance
	//par chronologie ce bean va etre executer avant son injction dans les aure class et service donc cette meme instance va etre toujours appelle dans le projet 
	//ce methode est depalcer dans la class de security "SecurityBeansConfiguration"
	/*@Bean
	BCryptPasswordEncoder getBCE() {
		return new BCryptPasswordEncoder();
	}*/


}
