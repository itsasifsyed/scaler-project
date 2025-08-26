package com.example.userauthenticationservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenDto {
    String token;
    Long userId;
}
