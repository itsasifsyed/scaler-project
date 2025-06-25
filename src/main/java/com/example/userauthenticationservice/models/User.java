package com.example.userauthenticationservice.models;

import com.example.userauthenticationservice.models.BaseModel;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User extends BaseModel {
    private String emailId;
    private String password;
}