package com.wap.task.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wap.task.model.Team;
import com.wap.task.model.User;
import com.wap.task.repository.TeamRepository;
import com.wap.task.service.TaskService;

@Controller
@RequestMapping(value = "team")
public class TeamController {
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    protected void team(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String JSONtasks = new Gson().toJson(taskService.getTeams());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONtasks);
    }
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String save(@RequestBody Map<String, String> input) {
		String name = input.get("teamName");
		Team team = new Team();
		team.setName(name);
		taskService.saveTeam(team);
		return new Gson().toJson(taskService.getTeams());
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String add(@RequestBody Map<String, String> input) {
		String id = input.get("teamId");
		String username = input.get("username");
		User user = taskService.getUserByUsername(username);
		Team team = taskService.getTeamById(Long.valueOf(id));
		user.setTeam(team);
		taskService.saveUser(user);
		return new Gson().toJson(taskService.getTeams());
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String delete(@RequestBody Map<String, String> input) {
		String id = input.get("teamId");
		Team team = taskService.getTeamById(Long.parseLong(id));
		taskService.deleteTeam(team);
		return new Gson().toJson(taskService.getTeams());
	}
	
}
