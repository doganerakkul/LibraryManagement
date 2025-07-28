package com.doganer.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AuthorRequestDTO {
    @NotBlank(message = "Yazar adı boş olamaz")
    private String name;

    private String date;

    private List<BookTitleDTO> books;

    @Data
    public static class BookTitleDTO {
        @NotBlank(message = "Kitap başlığı boş olamaz")
        private String title;
    }
}
