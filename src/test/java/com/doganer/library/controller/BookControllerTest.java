package com.doganer.library.controller;

import com.doganer.library.dto.BookRequestDTO;
import com.doganer.library.model.Author;
import com.doganer.library.model.Book;
import com.doganer.library.repository.AuthorRepository;
import com.doganer.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setup() {
        // DB’yi tamamen temizle
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // Gerçek koduna uygun örnek veriler
        author = authorRepository.save(
                new Author(null, "Jules Verne", "10/06/1995", List.of())
        );
        book1 = bookRepository.save(
                new Book(null, "Yirmi Bin Fersah", author)
        );
        book2 = bookRepository.save(
                new Book(null, "80 Günde Devri Âlem", author)
        );
    }

    @Test
    void createBook_shouldReturnCreatedBook() throws Exception {
        BookRequestDTO payload = new BookRequestDTO();
        payload.setTitle("Deniz Altında Yirmi Bin Fersah");
        payload.setAuthor(author);

        mockMvc.perform(post("/api/authors/{authorId}/books", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Deniz Altında Yirmi Bin Fersah"))
                .andExpect(jsonPath("$.author.id").value(author.getId()))
                .andExpect(jsonPath("$.author.name").value("Jules Verne"));
    }

    @Test
    void getAllBooks_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/book")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Yirmi Bin Fersah")))
                .andExpect(jsonPath("$[1].title", is("80 Günde Devri Âlem")));
    }

    @Test
    void updateBook_shouldChangeTitle() throws Exception {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setTitle("Deniz Altında Yirmi Bin Fersah");
        dto.setAuthor(author);
        String body = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        put("/api/book/{id}", book1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Yeni Başlık"))
                .andExpect(jsonPath("$.author.name").value("Jules Verne"));

    }

    @Test
    void deleteBook_shouldRemoveIt() throws Exception {
        mockMvc.perform(delete("/api/book/{id}", book2.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/book")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void searchBooksByTitle_shouldFilter() throws Exception {
        mockMvc.perform(get("/api/book/search")
                        .param("title", "Günde")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("80 Günde Devri Âlem")));
    }
}
