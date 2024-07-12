package com.example.LiterAlura.service;


import com.example.LiterAlura.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    List<Author> getLivingAuthorsInYear(int year);
}
