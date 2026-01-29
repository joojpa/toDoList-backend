package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.dtos.user.UserCreateDTO;
import br.com.todolist.toDoList.dtos.user.UserResponseDTO;
import br.com.todolist.toDoList.dtos.user.UserUpdateDTO;
import br.com.todolist.toDoList.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserCreateDTO userDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userDTO));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(
            @RequestAttribute("IdUser") Long userId) {

        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestAttribute("IdUser") Long userId,
            @RequestBody UserUpdateDTO userDTO) {

        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(
            @RequestAttribute("IdUser") Long userId) {

        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}