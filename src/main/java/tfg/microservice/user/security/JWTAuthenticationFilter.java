package tfg.microservice.user.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tfg.microservice.user.exception.AttemptAuthenticationException;
import tfg.microservice.user.model.User;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			User credenciales = new ObjectMapper().readValue(request.getInputStream(), User.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.getEmail(),
					credenciales.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			try {
				throw new AttemptAuthenticationException();
			} catch (AttemptAuthenticationException e1) {	
				return null;
			}
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String token = Jwts.builder().setSubject((auth.getName()))
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512,
						Base64.getEncoder().encodeToString(SecurityConstants.SUPER_SECRET_KEY.getBytes()))
				.compact();
		response.addHeader(SecurityConstants.HEADER_AUTHORIZACION_KEY,
				SecurityConstants.TOKEN_BEARER_PREFIX + " " + token);
	}
}