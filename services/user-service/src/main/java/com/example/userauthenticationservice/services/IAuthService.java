package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {
    User signup(String email, String password);

    Pair<User,String> login(String email, String password);
    Boolean validateToken(String token,Long userId);
    boolean logout(String token);
}
