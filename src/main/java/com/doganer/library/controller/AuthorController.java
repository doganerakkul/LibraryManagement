package com.doganer.library.controller;

import com.doganer.library.model.Author;
import com.doganer.library.model.Book;
import com.doganer.library.repository.AuthorRepository;
import com.doganer.library.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public Author createAuthor(@Valid @RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @Valid @RequestBody Author updated) {
        Author author = authorRepository.findById(id).orElseThrow();
        author.setName(updated.getName());
        return authorRepository.save(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @PostMapping("/{authorId}/books")
    public Book addBookToAuthor(@PathVariable Long authorId, @RequestBody Book book) {
        Author author = authorRepository.findById(authorId).orElseThrow();
        book.setAuthor(author);
        return bookRepository.save(book);
    }
}