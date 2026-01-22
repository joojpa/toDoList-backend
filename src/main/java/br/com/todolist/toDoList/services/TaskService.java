package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.Repository.TaskRepository;
import br.com.todolist.toDoList.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskEntity createTask(TaskEntity taskEntity, Long userId) {

        taskEntity.setIdUser(userId);

        validateDates(taskEntity.getStartAt(), taskEntity.getEndAt());

        return taskRepository.save(taskEntity);
    }

    public List<TaskEntity> listTasksByUser(Long userId) {
        return taskRepository.findByIdUser(userId);
    }

    public TaskEntity updateTask(Long taskId, Long userId, TaskEntity taskData) {

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        if (!task.getIdUser().equals(userId)) {
            throw new IllegalStateException("Você não pode alterar essa tarefa");
        }

        validateDates(taskData.getStartAt(), taskData.getEndAt());

        task.setTitle(taskData.getTitle());
        task.setDescription(taskData.getDescription());
        task.setStartAt(taskData.getStartAt());
        task.setEndAt(taskData.getEndAt());

        return taskRepository.save(task);
    }

    private void validateDates(LocalDateTime startAt, LocalDateTime endAt) {

        if (startAt == null || endAt == null) {
            throw new IllegalArgumentException("Datas de início e término são obrigatórias");
        }

        var now = LocalDateTime.now();

        if (startAt.isBefore(now)) {
            throw new IllegalArgumentException("A data de início não pode ser no passado");
        }

        if (endAt.isBefore(now)) {
            throw new IllegalArgumentException("A data de término não pode ser no passado");
        }

        if (endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("A data de término deve ser depois da data de início");
        }
    }
}