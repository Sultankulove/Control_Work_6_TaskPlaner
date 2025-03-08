package main.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TaskAddHandler implements HttpHandler {
    private final Configuration cfg;
    private final TaskManager taskManager;

    public TaskAddHandler(Configuration cfg, TaskManager taskManager) {
        this.cfg = cfg;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equalsIgnoreCase(method)) {
            handleGet(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String dateParam = "";
        if (query != null && query.contains("date=")) {
            String[] parts = query.split("=");
            if (parts.length > 1 && !parts[1].isBlank()) {
                dateParam = parts[1];
            } else {
                dateParam = LocalDate.now().toString();
            }
        } else {
            dateParam = LocalDate.now().toString();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("date", dateParam);

        Template template = cfg.getTemplate("taskAdd.ftl");
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody();
             OutputStreamWriter writer = new OutputStreamWriter(os)) {
            template.process(data, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder formData = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            formData.append(line);
        }

        String[] pairs = formData.toString().split("&");
        Map<String, String> params = new HashMap<>();
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }

        String name = params.getOrDefault("name", "");
        String description = params.getOrDefault("description", "");
        String type = params.getOrDefault("type", "other");
        String dateParam = params.getOrDefault("date", "");

        LocalDate date;
        try {
            date = LocalDate.parse(dateParam);
        } catch (Exception e) {
            date = LocalDate.now();
            dateParam = date.toString();
        }

        Task newTask = new Task();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setType(type);
        newTask.setDate(date);

        taskManager.addTask(newTask);

        exchange.getResponseHeaders().set("Location", "/tasks?date=" + dateParam);
        exchange.sendResponseHeaders(303, -1);
    }
}
