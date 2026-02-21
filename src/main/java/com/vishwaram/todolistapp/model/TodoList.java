package com.vishwaram.todolistapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TodoList")
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListItem> items;
    private LocalDate creationDate;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void addListItem(ListItem item) {
        item.setParent(this);
        this.items.add(item);

    }
}