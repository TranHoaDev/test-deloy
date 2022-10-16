package com.project.ecommerce.perfume.dto.auth;

import com.project.ecommerce.perfume.dto.user.UserResponse;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserResponse user;
    private String token;
}
