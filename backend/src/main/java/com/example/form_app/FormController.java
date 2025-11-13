package com.example.form_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class FormController {

    @Autowired
    private UserService userService;

    // Получение всех пользователей
    @GetMapping("/users")
    public ItemListResponse getUsers() {
        return userService.getItems();
    }

    // Добавление нового пользователя
    @PostMapping("/users")
    public String createUser(@RequestBody UserDTO user) {
        // В реальном приложении здесь должна быть валидация и сохранение в БД
        System.out.println("Добавлен пользователь: " + user.getFirstName() + " " + user.getLastName());
        return "Пользователь успешно добавлен";
    }

    // Старый эндпоинт (можно оставить для совместимости или удалить)
    @PostMapping("/submit")
    public String handleForm(@RequestBody FormData data) {
        System.out.println("Получены данные: " + data.getName() + ", " + data.getEmail());
        return "Данные получены: " + data.getName();
    }

    // Вложенный класс для старого эндпоинта
    public static class FormData {
        private String name;
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
