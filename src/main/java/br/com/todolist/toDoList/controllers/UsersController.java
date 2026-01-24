package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.entities.UserEntity;
import br.com.todolist.toDoList.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserEntity> createUser(
            @RequestBody UserEntity userEntity
    ) {
        var userCreated = userService.createUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}

