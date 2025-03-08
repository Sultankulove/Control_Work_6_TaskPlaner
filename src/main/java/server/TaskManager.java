package main.java.server;

import main.java.utils.FileUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = FileUtils.readTasks();
    }

    public synchronized void addTask(Task task) {
        tasks.add(task);
        FileUtils.writeTasks(tasks);
    }

    public synchronized void deleteTask(String name) {
        tasks = tasks.stream()
                .filter(task -> !task.getName().equals(name))
                .collect(Collectors.toList());
        FileUtils.writeTasks(tasks);
    }

    public synchronized List<Task> getTasksByDate(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public synchronized Task getTaskByName(String name) {
        return tasks.stream()
                .filter(task -> task.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public synchronized Task getTaskById(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void updateTask(Task updatedTask) {
        Task existingTask = getTaskByName(updatedTask.getName());
        if (existingTask != null) {
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setType(updatedTask.getType());
            existingTask.setDate(updatedTask.getDate());
            FileUtils.writeTasks(tasks);
        }
    }
}
