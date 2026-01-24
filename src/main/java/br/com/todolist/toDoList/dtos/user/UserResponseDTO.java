package br.com.todolist.toDoList.dtos.user;

public record UserResponseDTO(
        Long id,
        String name,
        String email
) {}