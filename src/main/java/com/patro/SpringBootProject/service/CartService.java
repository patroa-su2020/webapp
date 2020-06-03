package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.CartRepository;
import com.patro.SpringBootProject.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public void addBookToCart(Cart cart) {
        cartRepository.save(cart);
    }

    public List<Cart> getAllCartItems() {
        return (List<Cart>) cartRepository.findAll();
    }
}
