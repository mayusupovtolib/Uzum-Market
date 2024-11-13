package com.example.uzum_market.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull(message = "name is null")
    private String name;
    @NotNull(message = "firstName is null")
    private String firstName;
    @NotNull(message = "userName is null")
    private String userName;
    @NotNull(message = "password is null")
    private String password;
    @NotNull(message = "phoneNumber is null")
    private String phoneNumber;
    @NotNull(message = "phoneNumber is null")
    private String email;

}
