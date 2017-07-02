package com.lancel.bookshelf.api.controllers;

import com.lancel.bookshelf.api.repositories.BookRepositories;
import com.lancel.bookshelf.core.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Created by lancel on 02.07.2017.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepositories bookmarkRepositories;

    @Autowired
    BookController(BookRepositories bookmarkRepositories) {
        this.bookmarkRepositories = bookmarkRepositories;
    }

    @RequestMapping(value = "/id/{bookId}", method = RequestMethod.GET)
    Book findBookById(@PathVariable Long bookId) {
        return this.bookmarkRepositories.findOne(bookId);
    }

    @RequestMapping(value = "/isbn/{isbn}", method = RequestMethod.GET)
    Collection<Book> findBooksByISBN(@PathVariable String isbn) {
        return this.bookmarkRepositories.findBooksByIsbnContains(isbn);
    }

    @RequestMapping(value = "/author/{author}", method = RequestMethod.GET)
    Collection<Book> findBooksByAuthor(@PathVariable String author) {
        return this.bookmarkRepositories.findBooksByAuthorContains(author);
    }

    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET)
    Collection<Book> findBooksByTitle(@PathVariable String title) {
        return this.bookmarkRepositories.findBooksByTitleContains(title);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    Collection<Book> findBooksByTitleOrIsbnOrAuthor(@Param("title") String title, @Param("isbn") String isbn, @Param("author") String author) {
        return this.bookmarkRepositories.findAllByTitleContainsOrIsbnContainsOrAuthorContains(title, isbn, author);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addBook(@RequestBody Book book) {
        Book result = bookmarkRepositories.save(book);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/id/{bookId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        bookmarkRepositories.delete(bookId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/id/{bookId}", method = RequestMethod.PUT)
    ResponseEntity<?> updateBook(@RequestBody Book book) {
        Book repo = bookmarkRepositories.findOne(book.getId());
        repo.setAuthor(book.getAuthor());
        repo.setIsbn(book.getIsbn());
        repo.setTitle(book.getTitle());
        Book result = bookmarkRepositories.save(repo);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
