package br.com.todolist.toDoList.dtos.task;

import br.com.todolist.toDoList.enums.Status;

import java.time.LocalDateTime;

public record TaskCreateDTO(
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt
) {}

