package com.example.form_app.rest.service;

import com.example.form_app.rest.model.dto.ItemListResponseDto;
import com.example.form_app.rest.model.dto.UserDto;
import com.example.form_app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ObjectMapper objectMapper;

    public ItemListResponseDto getAllUsersFromFile() {
        try {
            File file = new ClassPathResource("users.json").getFile();
            return objectMapper.readValue(file, ItemListResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ItemListResponseDto addUser(UserDto user) {
        try {
            ItemListResponseDto itemListResponseDto = getAllUsersFromFile();
            if (user != null) {
                itemListResponseDto.getItems().add(user);
                String jsonData = Utils.objectToJsonString(itemListResponseDto);
                saveJsonStringToFile(jsonData, "users.json");
            }
            return itemListResponseDto;
        } catch (Exception e) {
            return null;
        }
    }

    public ItemListResponseDto deleteUser(String login) {
        try {

            ItemListResponseDto itemListResponseDto = getAllUsersFromFile();
            if (login == null)
                return itemListResponseDto;

            // Ищем нужного пользователя в списке
            UserDto userForDelete = null;
            for (UserDto user : itemListResponseDto.getItems()) {
                if (login.equalsIgnoreCase(user.getLogin())) {
                    userForDelete = user;
                }
            }

            if (userForDelete != null) {
                // Удаляем найденного в списке пользователя
                itemListResponseDto.getItems().remove(userForDelete);

                // Сохраняем изменение в файл
                String jsonData = Utils.objectToJsonString(itemListResponseDto);
                saveJsonStringToFile(jsonData, "users.json");
            }

            return itemListResponseDto;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveJsonStringToFile(String jsonData, String fileName) {
        try {
            File file = new ClassPathResource("users.json").getFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonData);
            writer.flush();
            System.out.println("Successfully wrote JSON data to " + file.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Error writing JSON data to file: " + e.getMessage());
        }
    }
}