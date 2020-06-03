package com.patro.SpringBootProject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "cart_data")
public class Cart {

    @Id
    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "buyer_id")
    private String buyerId;

    @Column(name = "quantity")
    private int quantity;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
