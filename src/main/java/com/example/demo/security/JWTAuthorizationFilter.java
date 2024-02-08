package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//class pour gerer les authorités 
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//"Authorization" : le JWT crée deja et envoye par la methode "successfulAuthentication" de la class "JWTAuthenticationFilter"
		//recuperer du header
		String jwt =request.getHeader("Authorization");
		
		//verifier la validité du token : "Bearer " c'est un prefixe utilisé d'habitude, mais c'est personalisé
		if (jwt==null || !jwt.startsWith(SecParams.PREFIX))
		{
			//passer au filter suivant
			filterChain.doFilter(request, response);
			//quitter la procedure d'ici
			 return;
		}
		
		//preparer un "JWTVerifier" pour verifier le JWT
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecParams.SECRET)).build();
		
		//enlever le préfixe Bearer du jwt
		// 7 caractères dans "Bearer "
		jwt= jwt.substring(SecParams.PREFIX.length()); 
		
		DecodedJWT decodedJWT = verifier.verify(jwt);
		
		//dans subject c'est le username
		String username = decodedJWT.getSubject();
		
		//get roles from claims
		List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
		
		//create authorities from roles
		Collection <GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (String r : roles)
		  authorities.add(new SimpleGrantedAuthority(r));
		
		UsernamePasswordAuthenticationToken user = 
				new UsernamePasswordAuthenticationToken(username,null,authorities);
		
		//dire a spring que c'est bon pour ce user il peut passe
		SecurityContextHolder.getContext().setAuthentication(user);
		
		//passer au filter suivant
		filterChain.doFilter(request, response);
	}
	
}
