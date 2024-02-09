package com.example.demo.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entities.User;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//class pour verifier l'authenticité : veut dire verifier le username et le password
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	
	private AuthenticationManager authenticationManager;
	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) 
	{
		super();
		this.authenticationManager = authenticationManager;
	}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException {
		
		//je vais extraire le user du request
		User user =null;
		try {
			//objectMapper pour serialise et deserialise a partir du json
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
				);
	}
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
			FilterChain chain, Authentication authResult) throws IOException, ServletException 
	{
		org.springframework.security.core.userdetails.User springUser = 
				(org.springframework.security.core.userdetails.User) authResult.getPrincipal();
		
		List<String> roles = new ArrayList<>();
		springUser.getAuthorities().forEach(au-> {
			roles.add(au.getAuthority());
		});
		
		//construction du JWT 
		String jwt = JWT.create()
			.withSubject(springUser.getUsername())
			.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
			.withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME))
			.sign(Algorithm.HMAC256(SecParams.SECRET));
		
		response.addHeader("Authorization", jwt); 
	}
	
	@Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException failed
			) 
					throws IOException, ServletException {
		
			if (failed instanceof DisabledException ) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType("application/json");
				Map<String, Object> data = new HashMap<>();
	
				data.put("errorCause", "disabled"); 
				data.put("message", "L'utilisateur est désactivé !");
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(data);
				PrintWriter writer = response.getWriter();
				writer.println(json);
				writer.flush();
	
			} else {
				super.unsuccessfulAuthentication(request, response, failed);
			}
	}

}
