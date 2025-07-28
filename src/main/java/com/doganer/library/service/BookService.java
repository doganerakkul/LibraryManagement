package com.doganer.library.service;

import com.doganer.library.model.Book;
import com.doganer.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }
    public List<Book> findAll() {
        return repository.findAll();
    }
    public List<Book> searchBooks(String title, String author) {
        if (title != null && author != null) {
            return repository.findByTitleContainingAndAuthorNameContaining(title, author);
        } else if (title != null) {
            return repository.findByTitleContaining(title);
        } else if (author != null) {
            return repository.findByAuthorNameContaining(author);
        } else {
            return findAll();
        }
    }
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }
    public Book save(Book book) {
        return repository.save(book);
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
