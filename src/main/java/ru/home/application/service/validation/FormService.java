package ru.home.application.service.validation;

import lombok.extern.slf4j.Slf4j;
import ru.home.application.model.dto.RequestFormDataDto;
import ru.home.application.model.dto.ResponseFormDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {

    private final ValidationService validationService;

    public ResponseEntity<ResponseFormDataDto> submit(RequestFormDataDto request) {
        log.info("Получены данные: {}, {}", request.getName(), request.getEmail());

        ResponseFormDataDto responseDto = new ResponseFormDataDto();
        responseDto.setName(request.getName());
        responseDto.setEmail(request.getEmail());

        boolean emailIsValid = validationService.checkEmail(request.getEmail());

        if (!emailIsValid) {
            responseDto.setErrorMessage("email не валиден. Ожидаемый формат: name@domen.ru");
        }

        return emailIsValid
                ? new ResponseEntity<>(responseDto, HttpStatus.OK)
                : new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

}