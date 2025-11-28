package org.example.springjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    String token;
    String newPassword;
}
