package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.Repository.UserRepository;
import br.com.todolist.toDoList.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public UserEntity createUser(@RequestBody UserEntity userEntity){

        var user = this.userRepository.findByEmail(userEntity.getEmail());

        if (userEntity.getEmail() == null){
            throw new RuntimeException("Email obrigatorio");
        }

        if (user != null){
            System.out.println("Usuario ja existe");
            return null;
        }
        return this.userRepository.save(userEntity);
    }
}
