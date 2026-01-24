package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.entities.UserEntity;
import br.com.todolist.toDoList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserEntity userEntity) {

        var userExists = userRepository.findByEmail(userEntity.getEmail());

        if (userExists.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Usuário já existe"
            );
        }

        userEntity.setPassword(
                passwordEncoder.encode(userEntity.getPassword())
        );

        return userRepository.save(userEntity);
    }
}
