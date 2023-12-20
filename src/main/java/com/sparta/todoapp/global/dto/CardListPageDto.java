package com.sparta.todoapp.global.dto;

import lombok.Getter;

@Getter
public class CardListPageDto {

    private int page;
    private int size;
    private String sortBy; // 완료 여부? 생성일?
    private boolean isAsc;

    public CardListPageDto(int page, int size, String sortBy, boolean isAsc) {
        this.page = page - 1;
        this.size = size;
        this.sortBy = sortBy;
        this.isAsc = isAsc;
    }
}
