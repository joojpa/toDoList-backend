package br.com.todolist.toDoList.Repository;

import br.com.todolist.toDoList.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
