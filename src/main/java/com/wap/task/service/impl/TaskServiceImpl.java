package com.wap.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wap.task.model.Task;
import com.wap.task.model.Team;
import com.wap.task.model.User;
import com.wap.task.repository.TaskRepository;
import com.wap.task.repository.TeamRepository;
import com.wap.task.repository.UserRepository;
import com.wap.task.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeamRepository teamRepository;
	
	// task
	@Override
	public List<Task> getTasks() {
		return taskRepository.findAll();
	}
	
	@Override
	public List<Task> getTasksByUsername(String username) {
		return taskRepository.findByUsername(username);
	}
	
	@Override
	public Task getTaskById(Long id) {
		return taskRepository.findOne(id);
	}

	@Override
	public Task saveTask(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public List<Task> getTasksByTeam(Team team) {
		return null; // TODO
	}
	
	@Override
	public List<Task> getTaskByUsers(List<String> usernames){
		return taskRepository.findByUsernameIn(usernames);
	}
	
	@Override
	public User getUserById(Long id) {
		return userRepository.findOne(id);
	}
	
	@Override
	public List<User> getUsersByTeam(Team team) {
		return userRepository.findByTeam(team);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public void deleteTask(Task task) {
		taskRepository.delete(task);
	}
	
	// user
	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	// team
	@Override
	public List<Team> getTeams() {
		List<Team> teams = teamRepository.findAll();
		for (Team team : teams) {
			List<String> usernames = userRepository.findByTeam(team).stream().map(User::getUsername).collect(Collectors.toList());
			team.setUsersnames(String.join(", ", usernames));
			
			List<String> tasks = getTaskByUsers(usernames).stream().map(Task::getTask).collect(Collectors.toList());
			team.setTasks(String.join(", ", tasks));
		}
		return teams;
	}

	@Override
	public Team getTeamById(Long id) {
		return teamRepository.findOne(id);
	}

	@Override
	public Team saveTeam(Team team) {
		return teamRepository.save(team);
	}
	
	@Override
	public void deleteTeam(Team team) {
		teamRepository.delete(team);
	}
}
