package com.example.form_app.rest.model.dto;

import java.util.List;

public class ItemListResponseDto {
    private List<UserDto> items;

    public ItemListResponseDto() {}

    public ItemListResponseDto(List<UserDto> items) {
        this.items = items;
    }

    public List<UserDto> getItems() {
        return items;
    }

    public void setItems(List<UserDto> items) {
        this.items = items;
    }
}