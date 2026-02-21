package com.vishwaram.todolistapp.controller;

import com.vishwaram.todolistapp.model.ListItemModel;
import com.vishwaram.todolistapp.model.TodoListModel;
import com.vishwaram.todolistapp.service.TodoListService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( value = "v1/todo", produces = { MediaType.APPLICATION_JSON_VALUE})
public class TodoListController {
    @Resource
    private TodoListService todoListService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TodoListModel>> getTodoList(@PathVariable("id") Long id) {
        return ResponseEntity.ok(todoListService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<TodoListModel>> getAllTodoList() {
        return ResponseEntity.ok(todoListService.getAll());
    }

    @PutMapping()
    public ResponseEntity<TodoListModel> addTodoList(@RequestBody TodoListModel list) {
        return ResponseEntity.ok(todoListService.addList(list));
    }

    @PutMapping("/{id}/item")
    public ResponseEntity<ListItemModel> addListItemModel(@PathVariable("id") Long id, @RequestBody ListItemModel list) {
        return ResponseEntity.ok(todoListService.addListItem(id, list));
    }
}
