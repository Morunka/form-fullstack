package com.example.form_app;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FormController {

    @PostMapping("/api/submit")
    public String handleForm(@RequestBody FormData data) {
        System.out.println("Получены данные: " + data.getName() + ", " + data.getEmail());
        return "Данные получены: " + data.getName();
    }

    public static class FormData {
        private String name;
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}