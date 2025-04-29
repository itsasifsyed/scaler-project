package com.example.productcatalogservice.modals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseModal {
    @Id
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private State state;
}
