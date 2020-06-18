package com.patro.SpringBootProject.model;

import javax.persistence.*;

@Entity
@Table(name = "image_data")
public class Image {

    @Id
    @Column(name = "image_id")
    private String imageId;

    @Column(name="book_id")
    private String bookId;

    @Column(name="image_url", length = 100000)
    private String imageUrl;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

//    public Book getBook() {
//        return book;
//    }
//
//    public void setBook(Book book) {
//        this.book = book;
//    }

    //    @Id
//    @Column(name = "book_id")
//    private String bookId;
//
//    @Column(name = "image_url")
//    private String imageUrls;
//
//    public String getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(String bookId) {
//        this.bookId = bookId;
//    }
//
//    public String getImageUrls() {
//        return imageUrls;
//    }
//
//    public void setImageUrls(String imageUrls) {
//        this.imageUrls = imageUrls;
//    }
}
