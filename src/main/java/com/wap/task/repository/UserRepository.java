package com.wap.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wap.task.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
