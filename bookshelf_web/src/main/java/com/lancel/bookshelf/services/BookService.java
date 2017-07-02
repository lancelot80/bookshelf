package com.lancel.bookshelf.services;

import com.lancel.bookshelf.core.entities.Book;

import java.util.List;

/**
 * Created by lancel on 29.06.2017.
 */
public interface BookService {

    List<Book> findAll(String searchAttribute, String searchText);
    void addBook(String title, String isbn, String author);
    void deleteBook(long id);
    void updateBook(Book book);
}
