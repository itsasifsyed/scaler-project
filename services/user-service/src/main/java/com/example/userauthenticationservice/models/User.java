package com.example.userauthenticationservice.models;

import com.example.userauthenticationservice.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "app_user")
public class User extends BaseModel {
    private String emailId;
    private String password;
}