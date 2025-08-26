package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.clients.KafkaProducerClient;
import com.example.userauthenticationservice.dtos.EmailDto;
import com.example.userauthenticationservice.exceptions.IncorrectPassword;
import com.example.userauthenticationservice.exceptions.UserAlreadyExists;
import com.example.userauthenticationservice.exceptions.UserNotFound;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User signup(String email, String password) {
        Optional<User> userOptional = userRepo.findUserByEmailId(email);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExists("Account already exists, please try logging in");
        }
        User user = new User();
        user.setPassword(password);
        user.setEmailId(email);

        //Sending welcome email
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(user.getEmailId());
        emailDto.setFrom("anuragbatch@gmail.com");
        emailDto.setBody("Thanks for signing. Have a great shopping experience.");
        emailDto.setSubject("Welcome to Scaler");

        try {
            kafkaProducerClient.sendMessage("signup", objectMapper.writeValueAsString(emailDto));
        }catch(JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return userRepo.save(user);
    }

    @Override
    public Pair<User,String> login(String email, String password){
        Optional<User> userOptional = userRepo.findUserByEmailId(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFound("User not found, please sign up first");
        }

        String storedPassword = userOptional.get().getPassword();
        if(!bCryptPasswordEncoder.matches(password,storedPassword)) {
            throw new IncorrectPassword("Please pass correct password, otherwise reset your password");
        }
        //Generating Token
//        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2025\"\n" + "}";
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);
        Map<String,Object> payload = new HashMap<>();
        payload.put("userId",userOptional.get().getId());
        Long currentTime = System.currentTimeMillis();
        payload.put("iat",currentTime);
        payload.put("exp",currentTime+100000);
        payload.put("iss","scaler");
        MacAlgorithm algo = Jwts.SIG.HS256;
        secretKey = algo.key().build();
        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();
        return new Pair<User,String>(userOptional.get(),token);
    }

    @Override
    public Boolean validateToken(String token,Long userId){
        try{
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            String newToken = Jwts.builder().claims(claims).signWith(secretKey).compact();
            if(!newToken.equals(token)){
                throw new RuntimeException("Invalid token");
            }
            Long expiry = (Long)claims.get("exp");
            Long currentTime = System.currentTimeMillis();
            if(currentTime > expiry) {
                System.out.println("Token has expired");
                throw new RuntimeException("Token has expired");
            }
            return true;
        } catch (Exception exception){
            throw exception;
        }

    }
    @Override
    public boolean logout(String token) {
        // TODO: Implement proper token validation and invalidation logic
        // For now, return true to indicate successful logout
        return true;
    }
}
