package com.example.form_app;

import com.example.form_app.UserDTO;

import java.util.List;

public class ItemListResponse {
    private List<UserDTO> items;

    public ItemListResponse() {}

    public ItemListResponse(List<UserDTO> items) {
        this.items = items;
    }

    public List<UserDTO> getItems() {
        return items;
    }

    public void setItems(List<UserDTO> items) {
        this.items = items;
    }
}
