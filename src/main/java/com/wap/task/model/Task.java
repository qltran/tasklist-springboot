package com.wap.task.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "task")
public class Task {
	@Expose(serialize = false, deserialize = false)
	private Long id;
	@Expose
	private String task;
	@Expose
	private String due;
	@Expose
	private String priority;
	@Expose
	private String category;
	@Expose
	private String complete;
	@Expose
	private String username;
	
	public Task update(String task, String due, String category) {
		this.task = task;
		this.due = due;
		this.category = category;
		return this;
	}
	
	public Task() {
	}
	
	public Task (String task, String due, String category) {
		this.task = task;
        this.due = due;
        this.category = category;
	}
	
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
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
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
	
	public String getComplete() {
		return complete;
	}
	public void setComplete(String status) {
		this.complete = status;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
