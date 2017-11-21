package com.wap.task.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wap.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
