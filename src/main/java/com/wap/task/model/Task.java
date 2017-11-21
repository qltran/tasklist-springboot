package com.wap.task.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "task")
public class Task {
	private Long id;
	private String task;
	private String due;
	private int priority;
	private String category;
	private String status;
	private User user;
	
	public Task(long id, String task, String due, String category) {
        this.id = id;
        this.task = task;
        this.due = due;
        this.category = category;
    }

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTask() {
		return task;
	}
	
	public void setTask(String name) {
		this.task = name;
	}
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getDue() {
		return due;
	}
	
	public void setDue(String due) {
		this.due = due;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
