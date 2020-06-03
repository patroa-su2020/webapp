package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.BookRepository;
import com.patro.SpringBootProject.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book)
    {
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public void deleteBookById(String bookId) {
        bookRepository.deleteById(bookId);
    }
}
