package com.capstoneProject.foodbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstoneProject.foodbox.model.Cart;
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
