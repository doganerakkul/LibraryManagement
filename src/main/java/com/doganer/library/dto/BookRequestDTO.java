package com.doganer.library.dto;

import com.doganer.library.model.Author;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequestDTO {
    @NotBlank(message = "Başlık boş olamaz")
    private String title;

    @NotBlank(message = "Yazar boş olamaz")
    private Author author;
}