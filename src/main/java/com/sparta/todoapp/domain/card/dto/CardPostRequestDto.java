package com.sparta.todoapp.domain.card.dto;

import com.sparta.todoapp.domain.card.entity.Card;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CardPostRequestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private MultipartFile file;

    public CardPostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
