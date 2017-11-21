package com.wap.task.controller;

import com.google.gson.Gson;
import com.wap.task.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping(value = "task")
public class TaskController {
	@RequestMapping(value = "", method = RequestMethod.GET)
    protected void tasks(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String JSONtasks;
        List<Task> taskList = new MockData().retrieveTaskList();
        JSONtasks = new Gson().toJson(taskList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONtasks);
    }
}
