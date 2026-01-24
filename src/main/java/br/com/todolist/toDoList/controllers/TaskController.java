package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.dtos.task.TaskCreateDTO;
import br.com.todolist.toDoList.dtos.task.TaskResponseDTO;
import br.com.todolist.toDoList.dtos.task.TaskUpdateDTO;
import br.com.todolist.toDoList.entities.TaskEntity;
import br.com.todolist.toDoList.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestBody TaskCreateDTO taskDTO,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("IdUser");

        TaskResponseDTO task = taskService.createTask(taskDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskResponseDTO>> list(
            @RequestAttribute("IdUser") Long userId
    ) {
        var tasks = taskService.listTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable Long id,
            @RequestAttribute("IdUser") Long userId,
            @RequestBody TaskUpdateDTO task,
            HttpServletRequest request){

                TaskResponseDTO updateTask = taskService.updateTask(id,userId,task);

                return ResponseEntity.ok(updateTask);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestAttribute("IdUser") Long userId
    ) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

}
