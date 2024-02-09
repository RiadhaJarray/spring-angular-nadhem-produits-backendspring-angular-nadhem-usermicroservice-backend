package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.service.UserService;

@Service
public class MyUserDetailsService  implements UserDetailsService{

	@Autowired
	UserService userService; 
	
	//cet methode va etre invoque automatiquement par spring security, lors d'un tentative de connexion
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//recherche user par son username
		User user = userService.findUserByUsername(username);
		
		if (user==null) 
			throw new UsernameNotFoundException("Utilisateur introuvable !");
		
		//GrantedAuthority : class d'authority : on va remplir la list a partir de la listes des roles des cet user
		//avec "eager" dans l'entity; l'utilisateur va etrre retourner avec son liste des roles :)
		List<GrantedAuthority> auths = new ArrayList<>();
		user.getRoles().forEach(role -> {
			//create "GrantedAuthority" a partir d'un "role.getRole()"
			GrantedAuthority auhority = new SimpleGrantedAuthority(role.getRole());
			auths.add(auhority);
		});
		
		//on retourne ici un objet user de type user de spring.userdetail 
		//nous avons donné un qualificatif complet car nous avons deja importer un User (user de entities): on ne peut pas importer un autre User
		//il demande : username , password , authorityList
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),user.getPassword(),user.getEnabled(),true,true,true,auths
				);
	}

}
