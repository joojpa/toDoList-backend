package br.com.todolist.toDoList.controllers;

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
    public ResponseEntity<?> create(@RequestBody TaskEntity taskEntity,
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("IdUser");

        var task = taskService.createTask(taskEntity, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskEntity>> list(HttpServletRequest request) {
        Long idUser = (Long) request.getAttribute("IdUser");
        var tasks = taskService.listTasksByUser(idUser);

        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody TaskEntity taskEntity, HttpServletRequest request){
                Long userId = (Long) request.getAttribute("IdUser");

                var updateTask = taskService.updateTask(id,userId,taskEntity);

                return ResponseEntity.ok(updateTask);
    }
}
