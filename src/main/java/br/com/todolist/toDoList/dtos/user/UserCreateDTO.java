package br.com.todolist.toDoList.dtos.user;

public record UserCreateDTO(
        String name,
        String email,
        String password
) {}

