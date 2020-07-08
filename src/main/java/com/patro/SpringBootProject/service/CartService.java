package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.CartRepository;
import com.patro.SpringBootProject.model.Cart;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StatsDClient statsDClient;

    private long startTime;
    private long endTime;

    public void addBookToCart(Cart cart) {
        startTime = System.currentTimeMillis();
        cartRepository.save(cart);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB add book to cart", startTime-endTime);
    }

    public List<Cart> getAllCartItems() {
        return (List<Cart>) cartRepository.findAll();
    }
}
