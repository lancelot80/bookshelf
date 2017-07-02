package com.lancel.bookshelf.services;

import com.lancel.bookshelf.core.entities.Book;
import com.lancel.bookshelf.web.HomePage;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lancel on 29.06.2017.
 */
@Service
public class BookServiceImpl implements BookService {

    private Logger log = LoggerFactory.getLogger(HomePage.class);

    final String uri = "http://localhost:4040/api/books";

    public List<Book> findAll(String searchFor) {

        RestTemplate restTemplate = new RestTemplate();
        String searchUri = uri + "/search?title=" + searchFor + "&isbn=" + searchFor + "&author=" + searchFor + "&id=" + searchFor;
        Book[] array = restTemplate.getForObject(searchUri, Book[].class);
        List<Book> books = Arrays.asList(array);
        return books;
    }

    @Override
    public List<Book> findAll(String searchAttribute, String searchText) {

        if (StringUtils.isBlank(searchAttribute)) {
            return findAll(searchText);
        } else if ("id".equals(searchAttribute)) {
            try {
                long searchId = Long.valueOf(searchText);
                return Arrays.asList(findById(searchId));
            } catch (Exception e) {
                return new ArrayList<>();
            }
        } else if ("title".equals(searchAttribute)) {
            return findByTitle(searchText);
        } else if ("author".equals(searchAttribute)) {
            return findByAuthor(searchText);
        } else if ("isbn".equals(searchAttribute)) {
            return findByISBN(searchText);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void addBook(String title, String isbn, String author) {

        RestTemplate restTemplate = new RestTemplate();

        JSONObject request = new JSONObject();
        request.put("title", title);
        request.put("isbn", isbn);
        request.put("author", author);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Book added successfully. " + request);
        }
    }

    @Override
    public void deleteBook(long id) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri + "/id/" + String.valueOf(id));
        log.info("Book deleted successfully. ");
    }

    @Override
    public void updateBook(Book book) {

        RestTemplate restTemplate = new RestTemplate();

        JSONObject request = new JSONObject();
        request.put("id", book.getId());
        request.put("title", book.getTitle());
        request.put("isbn", book.getIsbn());
        request.put("author", book.getAuthor());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        restTemplate.put(uri + "/id/" + book.getId(), entity);
        log.info("Book changed successfully. ");
    }

    public Book findById(long id) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            Book book = restTemplate.getForObject(uri + "/id/" + String.valueOf(id), Book.class);
            return book;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Book> findByAuthor(String author) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PagedResources<Book>> responseEntity = restTemplate.exchange(uri + "/search/findBooksByAuthorContains?author=" + author, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Book>>() {});
        PagedResources<Book> resources = responseEntity.getBody();
        List<Book> books = new ArrayList(resources.getContent());
        return books;
    }

    public List<Book> findByISBN(String isbn) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PagedResources<Book>> responseEntity = restTemplate.exchange(uri + "/search/findBooksByIsbnContains?isbn=" + isbn, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Book>>() {});
        PagedResources<Book> resources = responseEntity.getBody();
        List<Book> books = new ArrayList(resources.getContent());
        return books;
    }

    public List<Book> findByTitle(String title) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PagedResources<Book>> responseEntity = restTemplate.exchange(uri + "/search/findBooksByTitleContains?title=" + title, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Book>>() {});
        PagedResources<Book> resources = responseEntity.getBody();
        List<Book> books = new ArrayList(resources.getContent());
        return books;
    }
}
