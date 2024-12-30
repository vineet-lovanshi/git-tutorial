package com.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String header = request.getHeader("Authorization");
//		System.out.println("Token: "+header);
		String tokenString = null;
		String userName = null;

		if (header != null && header.startsWith("Bearer ")) {
			tokenString = header.substring(7);
//			System.out.println("Extracted Token: [" + tokenString + "]");

			userName = jwtService.extractUserName(tokenString);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userByUsername = myUserDetailsService.loadUserByUsername(userName);

			Boolean validateToken = jwtService.validatToken(tokenString, userByUsername);
			if (validateToken) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userByUsername,
						null, userByUsername.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);

	}

}
