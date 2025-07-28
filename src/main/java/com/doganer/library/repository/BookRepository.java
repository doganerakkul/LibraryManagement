package com.doganer.library.repository;

import com.doganer.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Kitap başlığına göre filtreler
    List<Book> findByTitleContaining(String title);

    // Yazar adının içinde geçenleri filtreler
    List<Book> findByAuthorNameContaining(String authorName);

    // Hem başlık hem yazar adına göre filtreler
    List<Book> findByTitleContainingAndAuthorNameContaining(String title, String authorName);
}
