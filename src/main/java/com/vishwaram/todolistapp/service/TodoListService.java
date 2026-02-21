package com.vishwaram.todolistapp.service;

import com.vishwaram.todolistapp.model.ListItem;
import com.vishwaram.todolistapp.model.ListItemModel;
import com.vishwaram.todolistapp.model.TodoList;
import com.vishwaram.todolistapp.model.TodoListModel;
import com.vishwaram.todolistapp.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TodoListService {
    @Autowired
    private TodoRepository todoRepository;

    public Optional<TodoListModel> getById(long id) {
        Optional<TodoList> list = todoRepository.findById(id);

        if (list.isEmpty()) {
            return Optional.empty();
        }

        TodoList concrete = list.get();
        return Optional.of(fromTodoList(concrete));
    }

    public List<TodoListModel> getAll() {
        List<TodoList> lists = todoRepository.findAll();
        return lists.stream().map(this::fromTodoList).toList();
    }

    public TodoListModel addList(TodoListModel newList) {
        // TODO: validate fields
        final TodoList entity = new TodoList();
        LocalDate creationDate = newList.creationDate();
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
        entity.setCreationDate(creationDate);
        fromListItemModel(newList.items())
                .forEach(i -> {
                    defaultListItemValues(i);
                    entity.addListItem(i);
                });

        TodoList saved = todoRepository.save(entity);
        return fromTodoList(saved);
    }

    public ListItemModel addListItem(long todoListId, ListItemModel newItem) {
        TodoList parent = todoRepository.findById(todoListId)
                .orElseThrow(() -> new EntityNotFoundException("Parent with Id does not exist: " + todoListId));

        ListItem newItemEntity = fromListItemModel(newItem);
        defaultListItemValues(newItemEntity);
        parent.addListItem(newItemEntity);
        parent = todoRepository.save(parent);
        newItemEntity = parent.getItems().get(parent.getItems().size()-1);
        return fromListItem(newItemEntity);
    }

    private void defaultListItemValues(ListItem item) {
        if (item.isDone() == null) {
            item.setDone(false);
        }

        if (item.getCreationDate() == null) {
            item.setCreationDate(LocalDate.now());
        }

        if (item.getUpdateDate() == null) {
            item.setUpdateDate(LocalDate.now());
        }
    }

    private Stream<ListItem> fromListItemModel(List<ListItemModel> items) {
        if (items == null) return Stream.empty();

        return items.stream().map(this::fromListItemModel);
    }

    private @NonNull TodoListModel fromTodoList(TodoList concrete) {
        Stream<ListItemModel> mapped = fromListItem(concrete.getItems());
        return new TodoListModel(concrete.getId(), mapped.toList(), concrete.getCreationDate());
    }

    private ListItem fromListItemModel(ListItemModel listItemModel) {
        ListItem item = new ListItem(listItemModel.isDone(), listItemModel.description());
        item.setCreationDate(listItemModel.creationDate());
        item.setUpdateDate(listItemModel.updateDate());
        return item;
    }
    private Stream<ListItemModel> fromListItem(List<ListItem> items) {
        if (items == null) return Stream.empty();
        return items.stream().map(this::fromListItem);
    }

    private ListItemModel fromListItem(ListItem item) {
        return new ListItemModel(
                item.getItemId(),
                item.getParent().getId(),
                item.isDone(),
                item.getDescription(),
                item.getCreationDate(),
                item.getUpdateDate()
        );
    }
}
