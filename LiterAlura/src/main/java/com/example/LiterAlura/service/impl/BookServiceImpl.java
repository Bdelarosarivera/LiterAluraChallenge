package com.example.LiterAlura.service.impl;

import com.example.LiterAlura.model.Book;
import com.example.LiterAlura.repository.BookRepository;
import com.example.LiterAlura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    @Override
    public Book getBookById(long id) {
        return null;
    }

    // Otros métodos que necesites...
}
