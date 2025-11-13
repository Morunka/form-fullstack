package com.example.form_app;

import org.springframework.stereotype.Service;
import com.example.form_app.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public ItemListResponse getItems() {

        List<UserDTO> items = new ArrayList<UserDTO>();

        UserDTO userIvan = new UserDTO();
        userIvan.setFirstName("Иван");
        userIvan.setLastName("Иванов");
        userIvan.setPatronymicName("Иванович");
        userIvan.setEmail("ivan@mail.ru");
        userIvan.setLogin("ivanovIvanIvanovich");
        userIvan.setPassword("passwordForIvan");
        items.add(userIvan);

        UserDTO userPetr = new UserDTO();
        userPetr.setFirstName("Петр");
        userPetr.setLastName("Петров");
        userPetr.setPatronymicName("Петрович");
        userPetr.setEmail("petr@mail.ru");
        userPetr.setLogin("petrovPetrPetrovich");
        userPetr.setPassword("passwordForPetr");
        items.add(userPetr);

        return new ItemListResponse(items);
    }
}
