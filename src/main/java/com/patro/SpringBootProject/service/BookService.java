package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.BookRepository;
import com.patro.SpringBootProject.model.Book;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StatsDClient statsDClient;

    private long startTime;
    private long endTime;

    public void saveBook(Book book)
    {
        startTime = System.currentTimeMillis();
        bookRepository.save(book);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: save book", endTime-startTime);
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public void deleteBookById(String bookId) {
        startTime = System.currentTimeMillis();
        bookRepository.deleteById(bookId);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: delete book", endTime-startTime);
    }
}
