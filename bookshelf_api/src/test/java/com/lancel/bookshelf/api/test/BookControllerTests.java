package com.lancel.bookshelf.api.test;

import com.lancel.bookshelf.api.controllers.BookController;
import com.lancel.bookshelf.api.repositories.BookRepositories;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lancel on 01.07.2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@FlywayTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class BookControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BookRepositories bookRepositories;

    @Test
    public void getBookByIdTest() throws Exception {
        this.mvc.perform(get("/books/id/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findById"));
    }

    @Test
    public void getBookByTitleTest() throws Exception {
        this.mvc.perform(get("/books/title/Core Java 2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findByTitle"));
    }

    @Test
    public void getBookByIsbnTest() throws Exception {
        this.mvc.perform(get("/books/isbn/444").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findByIsbn"));
    }

    @Test
    public void getBookByAuthorTest() throws Exception {
        this.mvc.perform(get("/books/author/eee aaa").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findByAuthor"));
    }

    @Test
    public void searchBooksByTitleOrIsbnOrAuthorTest() throws Exception {
        this.mvc.perform(get("/books/search?title=&isbn=&author=").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andDo(document("search"));
    }
}
