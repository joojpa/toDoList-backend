package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.dtos.task.TaskCreateDTO;
import br.com.todolist.toDoList.dtos.task.TaskResponseDTO;
import br.com.todolist.toDoList.dtos.task.TaskUpdateDTO;
import br.com.todolist.toDoList.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity<TaskResponseDTO> create(
            @RequestBody @Valid TaskCreateDTO taskDTO,
            @RequestAttribute("IdUser") Long userId) {

        TaskResponseDTO task = taskService.createTask(taskDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskResponseDTO>> list(
            @RequestAttribute("IdUser") Long userId) {

        return ResponseEntity.ok(taskService.listTasksByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid TaskUpdateDTO task,
            @RequestAttribute("IdUser") Long userId){

                return ResponseEntity.ok(taskService.updateTask(id,userId,task));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestAttribute("IdUser") Long userId) {

        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

}
