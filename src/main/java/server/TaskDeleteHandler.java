package main.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class TaskDeleteHandler implements HttpHandler {
    private final Configuration cfg;
    private final TaskManager taskManager;

    public TaskDeleteHandler(Configuration cfg, TaskManager taskManager) {
        this.cfg = cfg;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder formData = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                formData.append(line);
            }

            String[] pairs = formData.toString().split("&");
            String taskName = null;
            String dateParam = null;

            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                    if ("taskName".equals(key)) {
                        taskName = value;
                    } else if ("date".equals(key)) {
                        dateParam = value;
                    }
                }
            }

            if (taskName != null) {
                taskManager.deleteTask(taskName);
            }

            exchange.getResponseHeaders().set("Location", "/tasks?date=" + dateParam);
            exchange.sendResponseHeaders(303, -1);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
