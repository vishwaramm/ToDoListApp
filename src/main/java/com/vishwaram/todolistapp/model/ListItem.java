package com.vishwaram.todolistapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ListItem")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "todoList_id", nullable = false)
    private TodoList parent;
    private Boolean isDone;
    private String description;
    private LocalDate creationDate;
    private LocalDate updateDate;

    public ListItem() {}

    public ListItem(Boolean isDone, String description) {
        this.isDone = isDone;
        this.description = description;
    }

    public long getItemId() {
        return itemId;
    }

    public TodoList getParent() {
        return parent;
    }

    public void setParent(TodoList parent) {
        this.parent = parent;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean isDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}