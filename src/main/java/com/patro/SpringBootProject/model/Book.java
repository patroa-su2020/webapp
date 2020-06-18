package com.patro.SpringBootProject.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "book_data")
public class Book {

    @Id
    @Column(name = "book_id")
    private String bookId;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "create_dt_tm")
    private long createDateTime;

    @Column(name = "update_dt_tm")
    private long updateDateTime;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_id")
    private String sellerId;

//    @Column(name = "image")
//    private String imageURLs;

//    @OneToMany(mappedBy="book")
//    private Set<Image> images;

//    public Set<Image> getImages() {
//        return images;
//    }
//
//    public void setImages(Set<Image> images) {
//        this.images = images;
//    }

//    public String getImageURLs() {
//        return imageURLs;
//    }
//
//    public void setImageURLs(String imageURLs) {
//        this.imageURLs = imageURLs;
//    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public long getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(long updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String seller) {
        this.sellerName = seller;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price=" + price +
                ", createDateTime='" + createDateTime + '\'' +
                ", updateDateTime='" + updateDateTime + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }
}

