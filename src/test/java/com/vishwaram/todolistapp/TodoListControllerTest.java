package com.vishwaram.todolistapp;

import com.vishwaram.todolistapp.model.ListItem;
import com.vishwaram.todolistapp.model.ListItemModel;
import com.vishwaram.todolistapp.model.TodoList;
import com.vishwaram.todolistapp.model.TodoListModel;
import com.vishwaram.todolistapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoListControllerTest {
    /**
     *  @GetMapping("/{id}")
     *     public ResponseEntity<Optional<TodoListModel>> getTodoList(@PathVariable("id") Long id) {
     *         return ResponseEntity.ok(todoListService.getById(id));
     *     }
     *
     *     @GetMapping()
     *     public ResponseEntity<List<TodoListModel>> getAllTodoList() {
     *         return ResponseEntity.ok(todoListService.getAll());
     *     }
     *
     *     @PutMapping()
     *     public ResponseEntity<TodoListModel> addTodoList(@RequestBody TodoListModel list) {
     *         return ResponseEntity.ok(todoListService.addList(list));
     *     }
     *
     *     @PutMapping("/{id}/item")
     *     public ResponseEntity<ListItemModel> addListItemModel(@PathVariable("id") Long id, @RequestBody ListItemModel list) {
     *         return ResponseEntity.ok(todoListService.addListItem(id, list));
     *     }
     */

    @Autowired
    MockMvc mvc;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void getTodoListReturnsOne() throws Exception {
        insertTodoList();

        mvc.perform(get("/v1/todo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void getTodoListReturnsNone() throws Exception {
        mvc.perform(get("/v1/todo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.items").doesNotExist());
    }

    @Test
    void getAllTodoListReturnsTwo() throws Exception {
        insertTodoList();
        insertTodoList();
        mvc.perform(get("/v1/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists());
    }

    @Test
    void addTodoListWithNoError() throws Exception {
        TodoListModel todoList = getTodoListModel();

        mvc.perform(put("/v1/todo")
                        .content(objectMapper.writeValueAsString(todoList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void addItemToTodoListWithNoError() throws Exception {
        TodoList todo = insertTodoList();
        ListItemModel listItemModel = getListItemModel();

        mvc.perform(put("/v1/todo/" + todo.getId() +"/item")
                        .content(objectMapper.writeValueAsString(listItemModel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").exists());
    }

    private TodoList insertTodoList() {
        TodoList todo = getSampleTodoList();
        return todoRepository.save(todo);
    }

    private TodoList getSampleTodoList() {
        TodoList todo = new TodoList();
        todo.setCreationDate(LocalDate.now());
        todo.addListItem(new ListItem(false, "Test 1"));
        todo.addListItem(new ListItem(false, "Test 2"));
        return todo;
    }

    private TodoListModel getTodoListModel() {
        return new TodoListModel(
                1L,
                List.of(getListItemModel()),
                LocalDate.now()
        );
    }

    private ListItemModel getListItemModel() {
        return new ListItemModel(
                1L,
                1L,
                false,
                "Test 1",
                LocalDate.now(),
                LocalDate.now());

    }
}
