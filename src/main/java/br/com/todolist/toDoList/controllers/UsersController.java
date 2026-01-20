package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.entities.UserEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @PostMapping("/")
    public void createUser(@RequestBody UserEntity userEntity){
        System.out.println(userEntity.getName() + " " + userEntity.getEmail());
    }
}
