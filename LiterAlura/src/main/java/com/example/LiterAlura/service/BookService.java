package com.example.LiterAlura.service;

import com.example.LiterAlura.model.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    List<Book> getBooksByLanguage(String language);

    Book getBookById(long id);
}
