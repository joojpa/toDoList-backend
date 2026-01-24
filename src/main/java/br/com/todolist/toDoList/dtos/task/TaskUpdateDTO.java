package br.com.todolist.toDoList.dtos.task;

import java.time.LocalDateTime;

public record TaskUpdateDTO(
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt
) {}
