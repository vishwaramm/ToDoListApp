package com.vishwaram.todolistapp.repository;

import com.vishwaram.todolistapp.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoList, Long> {
}
