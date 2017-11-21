package com.wap.task.controller;

import com.google.gson.Gson;
import com.wap.task.model.Task;
import com.wap.task.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(value = "task")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    protected void task(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String JSONtasks = new Gson().toJson(taskService.getTasks());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONtasks);
    }
	
	@RequestMapping(value = "/filter", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String filter(@RequestBody Map<String, String> input) {
		String username = input.get("username");
		if (username == null || username.trim().isEmpty()) {
			return new Gson().toJson(taskService.getTasks());
		} else {
			
			return new Gson().toJson(taskService.getTasksByUsername(username.trim()));
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String save(@RequestBody Map<String, String> input) {
		String id = input.get("id");
		String name = input.get("task");
		String due = input.get("due");
		String category = input.get("category");
		String username = input.get("username");
		String priority = input.get("priority");
		Task task;
		if (id != null && !id.isEmpty()) {
			task = taskService.getTaskById(Long.parseLong(id));
			task.update(name, due, category);
		} else {
			task = new Task(input.get("task"), input.get("due"), input.get("category"));
		}
		task.setUsername(username);
		task.setPriority(priority);
		
		taskService.saveTask(task);
		return new Gson().toJson(taskService.getTasks());
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String complete(@RequestBody Map<String, String> input) {
		Task task = taskService.getTaskById(Long.parseLong(input.get("id")));
		task.setComplete(String.valueOf(true));
		taskService.saveTask(task);
		return new Gson().toJson(taskService.getTasks()); 
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
    protected void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Task task = taskService.getTaskById(Long.parseLong(request.getParameter("taskId")));
		taskService.deleteTask(task);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(taskService.getTasks()));
	}
}
