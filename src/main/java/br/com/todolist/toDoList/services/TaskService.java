package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.repository.TaskRepository;
import br.com.todolist.toDoList.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskEntity createTask(TaskEntity taskEntity, Long userId) {
        taskEntity.setIdUser(userId);
        validateDatesForCreate(taskEntity.getStartAt(), taskEntity.getEndAt());
        return taskRepository.save(taskEntity);
    }

    public List<TaskEntity> listTasksByUser(Long userId) {
        return taskRepository.findByIdUser(userId);
    }

    public TaskEntity updateTask(Long taskId, Long userId, TaskEntity taskData) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        if (!task.getIdUser().equals(userId)) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Tarefa não encontrada");
        }

        validateDatesForUpdate(taskData.getStartAt(), taskData.getEndAt());
        task.setTitle(taskData.getTitle());
        task.setDescription(taskData.getDescription());
        task.setStartAt(taskData.getStartAt());
        task.setEndAt(taskData.getEndAt());
        return taskRepository.save(task);
    }
    public void deleteTask(Long taskId, Long userId) {

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (!task.getIdUser().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tarefa não encontrada");
        }

        taskRepository.delete(task);
    }



    private void validateDatesForCreate(LocalDateTime startAt, LocalDateTime endAt) {

        if (startAt == null || endAt == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Datas de início e término são obrigatórias");
        }

        var now = LocalDateTime.now();

        if (startAt.isBefore(now)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de início não pode ser no passado");
        }

        if (endAt.isBefore(now)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de término não pode ser no passado");
        }

        if (endAt.isBefore(startAt)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de término deve ser depois da data de início");
        }
    }

    private void validateDatesForUpdate(LocalDateTime startAt, LocalDateTime endAt) {

        var now = LocalDateTime.now();

        if (startAt == null || endAt == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Datas obrigatórias");
        }

        if (endAt.isBefore(startAt)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Fim deve ser depois do início");
        }
        if (startAt.isBefore(now)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de início não pode ser no passado");
        }
    }
}