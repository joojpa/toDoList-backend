package br.com.todolist.toDoList.repository;

import br.com.todolist.toDoList.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByIdUser(Long idUser);
}
