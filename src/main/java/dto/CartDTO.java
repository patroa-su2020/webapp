package dto;

import com.patro.SpringBootProject.model.Book;


public class CartDTO {

    private Book book;
    private int quantity;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "book=" + book +
                ", quantity=" + quantity +
                '}';
    }
}

