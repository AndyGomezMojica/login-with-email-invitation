package com.andygomez.register_login.flow.infrastructure;

import com.andygomez.register_login.flow.application.TokenUseCase;
import com.andygomez.register_login.flow.model.MailModel;
import com.andygomez.register_login.flow.model.UserModel;
import com.andygomez.register_login.flow.model.repository.UserRepository;
import com.andygomez.register_login.flow.web.model.UserInput;
import com.andygomez.register_login.flow.web.model.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Slf4j
public class UserAdapter {

    @Autowired
    private TokenUseCase tokenUseCase;

    @Autowired
    private UserRepository repository;

    public UserModel inputToModel(UserInput input, String uuidPassword){
        return UserModel.builder()
                .userName(generateUserName(input.getName(), input.getLastName()))
                .name(input.getName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(tokenUseCase.encrypt(uuidPassword))
                .createdBy("ADMIN")
                .createdAt(new Date())
                .isActive(true)
                .build();
    }

    public UserResponse inputToResponse(UserInput input, String uuidPassword){
        return UserResponse.builder()
                .username(generateUserName(input.getName(), input.getLastName()))
                .password(uuidPassword)
                .build();

    }

    public void updatePasswordByResponse(UserModel user, String newPassword) {
        user.setPassword(tokenUseCase.encrypt(newPassword));
        user.setModifiedAt(new Date());
        user.setModifiedBy("USER");
    }

    public MailModel emailUserResponseStructure(String email, String userName, String uuidPassword){
        return MailModel.builder()
                .subject(email)
                .message("Username: " + userName + " Generic Password: " + uuidPassword)
                .build();
    }

    public String generateUserName(String name, String lastName){
        String actualName = name.substring(0,1);
        String cleanedLastName = lastName.replace(" ", "");
        return actualName + cleanedLastName.toUpperCase();
    }
}
