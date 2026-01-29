package br.com.todolist.toDoList.dtos.user;

public record UserUpdateDTO(
        String name,
        String email,
        String password
) {}