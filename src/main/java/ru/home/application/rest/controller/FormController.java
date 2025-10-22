package ru.home.application.rest.controller;

import ru.home.application.model.dto.RequestFormDataDto;
import ru.home.application.model.dto.ResponseFormDataDto;
import ru.home.application.service.validation.FormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @PostMapping("/submit")
    public ResponseEntity<ResponseFormDataDto> submit(@RequestBody RequestFormDataDto request) {
        return formService.submit(request);
    }

}