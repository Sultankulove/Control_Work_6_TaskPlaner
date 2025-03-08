package main.java.server;

import java.time.LocalDate;

public class Task {
    private long id;
    private String name;
    private String description;
    private String type;
    private LocalDate date;

    public Task(long id, String name, String description, String type, LocalDate date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public Task() {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }
}
