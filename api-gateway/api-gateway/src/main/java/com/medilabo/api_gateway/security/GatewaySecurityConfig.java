package com.medilabo.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
public class GatewaySecurityConfig {
	



	 @Bean
	    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

		 return http
			        .csrf(ServerHttpSecurity.CsrfSpec::disable)
			        .authorizeExchange(ex -> ex
			            .pathMatchers("/**").permitAll()
			        )			  
			        .build();
	    }
	 
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 

	
}
