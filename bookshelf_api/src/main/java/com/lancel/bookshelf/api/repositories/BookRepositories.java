package com.lancel.bookshelf.api.repositories;

import com.lancel.bookshelf.core.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by lancel on 29.06.2017.
 */
public interface BookRepositories extends CrudRepository<Book, Long> {

    @RestResource(path = "findBooksByTitleContains", rel = "findBooksByTitleContains")
    List<Book> findBooksByTitleContains(@Param("title") String title);

    @RestResource(path = "findBooksByIsbnContains", rel = "findBooksByIsbnContains")
    List<Book> findBooksByIsbnContains(@Param("isbn") String isbn);

    @RestResource(path = "findBooksByAuthorContains", rel = "findBooksByAuthorContains")
    List<Book> findBooksByAuthorContains(@Param("author") String author);

    @RestResource(path = "findAllByTitleContainsOrIsbnContainsOrAuthorContains", rel = "findAllByTitleContainsOrIsbnContainsOrAuthorContains")
    List<Book> findAllByTitleContainsOrIsbnContainsOrAuthorContains(@Param("title") String title, @Param("isbn") String isbn, @Param("author") String author);
}
