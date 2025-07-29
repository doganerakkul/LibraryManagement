package com.doganer.library.controller;

import com.doganer.library.dto.BookRequestDTO;
import com.doganer.library.dto.BookResponseDTO;
import com.doganer.library.model.Book;
import com.doganer.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping
    public List<BookResponseDTO> getAll() {
        return service.findAll().stream()
                .map(book -> {
                    BookResponseDTO dto = new BookResponseDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    return dto;
                }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(book -> {
                    BookResponseDTO dto = new BookResponseDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<BookResponseDTO> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        return service.searchBooks(title, author).stream()
                .map(book -> {
                    BookResponseDTO dto = new BookResponseDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    return dto;
                }).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO request) {
        Book newBook = new Book();
        newBook.setTitle(request.getTitle());
        newBook.setAuthor(request.getAuthor());

        Book saved = service.save(newBook);

        BookResponseDTO response = service.create(request);
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setAuthor(saved.getAuthor());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(
            @PathVariable Long id,
            @RequestBody BookRequestDTO request
    ) {
        return service.findById(id)
                .map(existing -> {
                    existing.setTitle(request.getTitle());
                    existing.setAuthor(request.getAuthor());
                    Book updated = service.save(existing);

                    var resp = new BookResponseDTO();
                    resp.setId(updated.getId());
                    resp.setTitle(updated.getTitle());
                    resp.setAuthor(updated.getAuthor());
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}


