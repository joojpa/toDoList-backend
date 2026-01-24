package br.com.todolist.toDoList.services;

import br.com.todolist.toDoList.dtos.task.TaskCreateDTO;
import br.com.todolist.toDoList.dtos.task.TaskResponseDTO;
import br.com.todolist.toDoList.dtos.task.TaskUpdateDTO;
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

    public TaskResponseDTO createTask(TaskCreateDTO taskDTO, Long userId) {
        validateDatesForCreate(taskDTO.startAt(), taskDTO.endAt());

        TaskEntity task = new TaskEntity();
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setStartAt(taskDTO.startAt());
        task.setEndAt(taskDTO.endAt());
        task.setIdUser(userId);


        return mapToResponse(taskRepository.save(task));
    }

    public List<TaskResponseDTO> listTasksByUser(Long userId) {
        return taskRepository.findByIdUser(userId).stream().map(this::mapToResponse).toList();
    }

    public TaskResponseDTO updateTask(Long taskId, Long userId, TaskUpdateDTO taskDTO) {
        TaskEntity task = taskRepository.findByIdAndIdUser(taskId,userId).
                orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        validateDatesForUpdate(taskDTO.startAt(), taskDTO.endAt());
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setStartAt(taskDTO.startAt());
        task.setEndAt(taskDTO.endAt());

        return mapToResponse(taskRepository.save(task));
    }
    public void deleteTask(Long taskId, Long userId) {

        TaskEntity task = taskRepository.findByIdAndIdUser(taskId,userId).
                orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

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

    }private TaskResponseDTO mapToResponse(TaskEntity task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt()
        );
    }



}