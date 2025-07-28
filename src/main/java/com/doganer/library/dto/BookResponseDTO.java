package com.doganer.library.dto;

import com.doganer.library.model.Author;
import lombok.Data;
@Data
public class BookResponseDTO {
    private Long id;
    private String title;
    private Author author;

}

