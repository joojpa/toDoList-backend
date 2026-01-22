package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.Repository.TaskRepository;
import br.com.todolist.toDoList.entities.TaskEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TaskEntity taskEntity, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("IdUser");
        taskEntity.setIdUser(userId);

        var now = LocalDateTime.now();
        if (taskEntity.getStartAt() == null || taskEntity.getEndAt() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Datas de início e término são obrigatórias");
        }

        if (taskEntity.getStartAt().isBefore(now)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início não pode ser no passado");
        }

        if (taskEntity.getEndAt().isBefore(now)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de término não pode ser no passado");
        }

        if (taskEntity.getEndAt().isBefore(taskEntity.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de término deve ser depois da data de início");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(taskEntity));
    }
}
