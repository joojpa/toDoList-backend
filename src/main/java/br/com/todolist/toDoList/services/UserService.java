package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.dtos.user.UserCreateDTO;
import br.com.todolist.toDoList.dtos.user.UserResponseDTO;
import br.com.todolist.toDoList.dtos.user.UserUpdateDTO;
import br.com.todolist.toDoList.entities.UserEntity;
import br.com.todolist.toDoList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UserResponseDTO createUser(UserCreateDTO userDTO) {
        validateUserData(userDTO.name(), userDTO.email(), userDTO.password());

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

        return mapToResponse(userRepository.save(user));
    }

    public UserResponseDTO getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        return mapToResponse(user);
    }

    public UserResponseDTO updateUser(Long userId, UserUpdateDTO userDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));


        if (userDTO.name() != null && !userDTO.name().isBlank()) {
            validateName(userDTO.name());
            user.setName(userDTO.name());
        }

        if (userDTO.email() != null && !userDTO.email().isBlank()) {
            validateEmail(userDTO.email());

            userRepository.findByEmail(userDTO.email())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userId)) {
                            throw new ResponseStatusException(
                                    HttpStatus.CONFLICT,
                                    "Email já está em uso"
                            );
                        }
                    });

            user.setEmail(userDTO.email());
        }

        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            validatePassword(userDTO.password());
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        userRepository.delete(user);
    }

    private void validateUserData(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome é obrigatório"
            );
        }

        if (name.length() < 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nome deve ter no mínimo 3 caracteres"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email é obrigatório"
            );
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email inválido"
            );
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Senha é obrigatória"
            );
        }

        if (password.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Senha deve ter no mínimo 6 caracteres"
            );
        }
    }

    private UserResponseDTO mapToResponse(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}