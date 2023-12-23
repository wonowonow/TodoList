package com.sparta.todoapp.domain.card.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
public class CardPostRequestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private MultipartFile file;
}
