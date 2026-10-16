package com.fpoly.java5.jpas;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.java5.entities.CartEntity;

public interface CartJPA extends JpaRepository<CartEntity, Integer>{

}
