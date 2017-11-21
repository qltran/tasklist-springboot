package com.wap.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wap.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	
	public List<Task> findByUsernameIn(List<String> usernames);
	
	public List<Task> findByUsername(String username);
}
