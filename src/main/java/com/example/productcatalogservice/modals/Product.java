package com.example.productcatalogservice.modals;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseModal implements Serializable {
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
    private Boolean isPrime;

    public Product(){
        this.setCreatedAt(new Date());
        this.setUpdatedAt(new Date());
        this.setState(State.ACTIVE);
    }
}


