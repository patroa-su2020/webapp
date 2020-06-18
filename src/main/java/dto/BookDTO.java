package dto;

import com.patro.SpringBootProject.model.Book;
import com.patro.SpringBootProject.model.Image;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;


public class BookDTO {
//
//    private String bookId;
//
//    private String isbn;
//
//    private String title;
//
//    private String authors;
//
//    private String publicationDate;
//
//    private String quantity;
//
//    private double price;
//
//    private String createDateTime;
//
//    private String updateDateTime;
//
//    private String seller;
//
//    public BookDTO() {
//    }
//
//    public String getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(String bookId) {
//        this.bookId = bookId;
//    }
//
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getAuthors() {
//        return authors;
//    }
//
//    public void setAuthors(String authors) {
//        this.authors = authors;
//    }
//
//    public String getPublicationDate() {
//        return publicationDate;
//    }
//
//    public void setPublicationDate(String publicationDate) {
//        this.publicationDate = publicationDate;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public String getCreateDateTime() {
//        return createDateTime;
//    }
//
//    public void setCreateDateTime(String createDateTime) {
//        this.createDateTime = createDateTime;
//    }
//
//    public String getUpdateDateTime() {
//        return updateDateTime;
//    }
//
//    public void setUpdateDateTime(String updateDateTime) {
//        this.updateDateTime = updateDateTime;
//    }
//
//    public String getSeller() {
//        return seller;
//    }
//
//    public void setSeller(String seller) {
//        this.seller = seller;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    @Override
//    public String toString() {
//        return "BookDTO{" +
//                "bookId='" + bookId + '\'' +
//                ", isbn='" + isbn + '\'' +
//                ", title='" + title + '\'' +
//                ", authors='" + authors + '\'' +
//                ", publicationDate='" + publicationDate + '\'' +
//                ", quantity='" + quantity + '\'' +
//                ", price=" + price +
//                ", createDateTime='" + createDateTime + '\'' +
//                ", updateDateTime='" + updateDateTime + '\'' +
//                ", seller='" + seller + '\'' +
//                '}';
//    }


    private Book book;
    private boolean isSeller;
    private List<Image> images;

    public BookDTO(Book book, boolean isSeller) {
        this.book = book;
        this.isSeller = isSeller;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "book=" + book +
                ", isSeller=" + isSeller +
                '}';
    }
}
