package br.com.todolist.toDoList.controllers;

import br.com.todolist.toDoList.Repository.TaskRepository;
import br.com.todolist.toDoList.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public TaskEntity create(@RequestBody TaskEntity taskEntity){
         return this.taskRepository.save(taskEntity);
    }
}
