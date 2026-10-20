package com.fpoly.java5.jpas;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.java5.entities.OrderItemEntity;

public interface OrderItemJPA extends JpaRepository<OrderItemEntity, Integer>{

}
