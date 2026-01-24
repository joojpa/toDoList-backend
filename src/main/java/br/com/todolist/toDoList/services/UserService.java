package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.dtos.user.UserCreateDTO;
import br.com.todolist.toDoList.dtos.user.UserResponseDTO;
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

    public UserResponseDTO createUser(UserCreateDTO userDTO) {

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Usuário já existe"
            );
        }

        UserEntity user = new UserEntity();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        UserEntity saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }
}
