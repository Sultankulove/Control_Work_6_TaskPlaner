package main.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TaskListHandler implements HttpHandler {
    private final Configuration cfg;
    private final TaskManager taskManager;

    public TaskListHandler(Configuration cfg, TaskManager taskManager) {
        this.cfg = cfg;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
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

            LocalDate date;
            try {
                date = LocalDate.parse(dateParam, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException ex) {
                date = LocalDate.now();
                dateParam = date.toString();
            }

            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'года'", new Locale("ru"));
            String formattedDate = date.format(displayFormatter);
            String isoDate = date.toString();

            List<Task> tasks = taskManager.getTasksByDate(date);

            Map<String, Object> data = new HashMap<>();
            data.put("dateDisplay", formattedDate);
            data.put("dateIso", isoDate);
            data.put("tasks", tasks);

            Template template = cfg.getTemplate("taskList.ftl");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody();
                 OutputStreamWriter writer = new OutputStreamWriter(os)) {
                template.process(data, writer);
            } catch (TemplateException e) {
                e.printStackTrace();
                ResponseCodes.sendErrorResponse(exchange, ResponseCodes.INTERNAL_SERVER_ERROR, "Ошибка при обработке шаблона");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseCodes.sendErrorResponse(exchange, ResponseCodes.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        }
    }
}
