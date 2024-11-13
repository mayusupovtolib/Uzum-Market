package com.example.uzum_market.controller;

import com.example.uzum_market.annotations.CurrentUser;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.payload.dto.UserDto;
import com.example.uzum_market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public HttpEntity<?> aboutMe(@CurrentUser User currentUser) {
        return userService.aboutMe(currentUser);
    }

    @PutMapping("/me")
    public HttpEntity<?> editMe(@RequestBody UserDto userDto, @CurrentUser User user) {
        return userService.editMe(userDto, user);
    }


    @DeleteMapping("/me")
    public HttpEntity<?> deleteAccount(@CurrentUser User currentUser) {
        return userService.deleteAccount(currentUser);

    }


}
