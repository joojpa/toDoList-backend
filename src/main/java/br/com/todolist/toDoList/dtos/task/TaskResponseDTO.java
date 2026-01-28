package br.com.todolist.toDoList.dtos.task;

import br.com.todolist.toDoList.enums.Status;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Status status
){}