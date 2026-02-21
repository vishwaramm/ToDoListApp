package com.vishwaram.todolistapp.model;


import java.time.LocalDate;

public record ListItemModel(Long itemId, Long todoListId, Boolean isDone, String description, LocalDate creationDate, LocalDate updateDate) {}

