package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.example.checkscam.entity.User}
 */
@AllArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    private  Long id;
    private  String name;
    private  String email;
    private  String refreshToken;
    private  String password;
}