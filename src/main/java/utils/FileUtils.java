package main.java.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.server.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final Path TASKS_FILE = Path.of("data/json/tasks.json");

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public static List<Task> readTasks() {
        if (Files.exists(TASKS_FILE)) {
            try {
                String json = Files.readString(TASKS_FILE);
                Task[] taskArray = gson.fromJson(json, Task[].class);
                if (taskArray != null) {
                    return new ArrayList<>(List.of(taskArray));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public static void writeTasks(List<Task> tasks) {
        try {
            String json = gson.toJson(tasks);
            Files.writeString(TASKS_FILE, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
