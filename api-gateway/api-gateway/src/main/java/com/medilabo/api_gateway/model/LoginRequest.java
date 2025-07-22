package com.medilabo.api_gateway.model;

import lombok.Data;

@Data
public class LoginRequest {
	
	private String username;
    private String password;

}
