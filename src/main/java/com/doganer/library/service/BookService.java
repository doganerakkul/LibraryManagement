package com.doganer.library.service;

import com.doganer.library.dto.BookRequestDTO;
import com.doganer.library.dto.BookResponseDTO;
import com.doganer.library.model.Author;
import com.doganer.library.model.Book;
import com.doganer.library.repository.BookRepository;
import jakarta.validation.Valid;
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
    public BookResponseDTO create(BookRequestDTO request) {
        Book newBook = new Book();
        newBook.setTitle(request.getTitle());
        newBook.setAuthor(request.getAuthor());

        Book saved = repository.save(newBook);

        BookResponseDTO response = new BookResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setAuthor(saved.getAuthor());

        return response;
    }

    public List<Book> searchBooks(String title, String author) {
        if (title != null && author != null) {
            return repository.findByTitleContainingAndAuthor_NameContaining(title, author);
        } else if (title != null) {
            return repository.findByTitleContaining(title);
        } else if (author != null) {
            return repository.findByAuthor_NameContaining(author);
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
