package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.Repository.UserRepository;
import br.com.todolist.toDoList.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserEntity userEntity){

        var user = this.userRepository.findByEmail(userEntity.getEmail());

        if (user.isPresent()){
            return ResponseEntity.status(409).body("Usuario já existe");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

         var userCreated = this.userRepository.save(userEntity);
        return ResponseEntity.status(201).body(userCreated);
    }
}
