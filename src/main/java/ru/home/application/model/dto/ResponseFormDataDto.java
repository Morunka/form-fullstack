package ru.home.application.model.dto;

import lombok.Data;

@Data
public class ResponseFormDataDto {

    private String name;
    private String email;
    private String errorMessage;

}