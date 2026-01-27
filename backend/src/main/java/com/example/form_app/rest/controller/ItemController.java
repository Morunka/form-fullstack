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

    @GetMapping("/api/users")
    public ItemListResponseDto getItems() {
        return userService.getAllUsersFromFile();
    }

    @PostMapping("/api/users/add")
    public ItemListResponseDto postItemsAdd(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PostMapping("/api/users/update")
    public ItemListResponseDto postItemsUpdate(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/api/users/delete")
    public ItemListResponseDto postItemsAdd(@RequestParam String login) {
        return userService.deleteUser(login);
    }
}
