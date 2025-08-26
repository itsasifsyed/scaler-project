package com.example.productcatalogservice.modals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseModalRepository<T extends BaseModal> extends JpaRepository<T, Long> {
}