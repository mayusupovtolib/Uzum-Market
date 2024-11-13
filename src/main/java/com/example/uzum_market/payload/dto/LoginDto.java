package com.example.uzum_market.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotNull(message = "userName is null")
    private String emailOrUsername;
    @NotNull(message = "password is null")
    private String password;

}
