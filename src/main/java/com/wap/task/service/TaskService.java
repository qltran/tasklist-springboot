package com.wap.task.service;

import java.util.List;

import com.wap.task.model.Task;
import com.wap.task.model.Team;
import com.wap.task.model.User;

public interface TaskService {
	// task
	public List<Task> getTasks();
	public List<Task> getTaskByUsers(List<String> usernames);
	public Task getTaskById(Long id);
	public Task saveTask(Task task);
	public List<Task> getTasksByTeam(Team team);
	public void deleteTask(Task task);
	public List<Task> getTasksByUsername(String username);
	// user
	public List<User> getUsers();
	public List<User> getUsersByTeam(Team team);
	public User getUserById(Long id);
	public User saveUser(User user);
	public User getUserByUsername(String username);
	
	// team
	public List<Team> getTeams();
	public Team getTeamById(Long id);
	public Team saveTeam(Team team);
	public void deleteTeam(Team team);
}
