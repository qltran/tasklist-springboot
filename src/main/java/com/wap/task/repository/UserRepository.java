package com.wap.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wap.task.model.Team;
import com.wap.task.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public User findByUsername(String username);
	public List<User> findByTeam(Team team);
}
