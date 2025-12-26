package com.example.form_app.rest.controller;

import com.example.form_app.rest.model.dto.ItemListResponseDto;
import com.example.form_app.rest.model.dto.UserDto;
import com.example.form_app.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final UserService userService;

    @GetMapping("/api/items")
    public ItemListResponseDto getItems() {
        return userService.getAllUsersFromFile();
    }

    @PostMapping("/api/items/add")
    public ItemListResponseDto postItemsAdd(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/api/items/delete")
    public ItemListResponseDto postItemsAdd(@RequestParam String login) {
        return userService.deleteUser(login);
    }
}
