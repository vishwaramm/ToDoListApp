package com.vishwaram.todolistapp.model;


import java.time.LocalDate;
import java.util.List;

public record TodoListModel(Long id, List<ListItemModel> items, LocalDate creationDate) {
}
