package main.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class CalendarPageHandler implements HttpHandler {
    private final Configuration cfg;
    private final TaskManager taskManager;

    public CalendarPageHandler(Configuration cfg, TaskManager taskManager) {
        this.cfg = cfg;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        String lastVisited = null;
        if (cookieHeader != null) {
            for (String c : cookieHeader.split(";\\s*")) {
                if (c.startsWith("lastVisitedCalendar=")) {
                    lastVisited = c.substring("lastVisitedCalendar=".length());
                }
            }
        }
        System.out.println("Cookie lastVisitedCalendar: " + lastVisited);
        String newCookieValue = LocalDate.now().toString();
        exchange.getResponseHeaders().add("Set-Cookie", "lastVisitedCalendar=" + newCookieValue + "; path=/; Max-Age=86400");

        Map<String, Object> data = new HashMap<>();

        try {
            LocalDate currentDate = LocalDate.now();
            String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru"));
            int year = currentDate.getYear();
            String formattedDate = month.toLowerCase() + " " + year;
            data.put("currentDate", formattedDate);

            List<List<Day>> weeks = new ArrayList<>();
            List<Day> currentWeek = new ArrayList<>();

            LocalDate startOfMonth = currentDate.withDayOfMonth(1);
            LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            LocalDate date = startOfMonth;
            while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
                date = date.minusDays(1);
            }

            while (!date.isAfter(endOfMonth)) {
                Day day = new Day();
                day.setDayOfMonth(date.getDayOfMonth());
                day.setCurrentDay(date.equals(currentDate));
                day.setTasks(taskManager.getTasksByDate(date));
                day.setDate(date.toString());
                currentWeek.add(day);
                if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    weeks.add(currentWeek);
                    currentWeek = new ArrayList<>();
                }
                date = date.plusDays(1);
            }

            if (!currentWeek.isEmpty()) {
                while (currentWeek.size() < 7) {
                    Day day = new Day();
                    day.setDayOfMonth(date.getDayOfMonth());
                    day.setTasks(Collections.emptyList());
                    day.setDate("");
                    currentWeek.add(day);
                    date = date.plusDays(1);
                }
                weeks.add(currentWeek);
            }

            data.put("weeks", weeks);

            Template template = cfg.getTemplate("calendar.ftl");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody();
                 OutputStreamWriter writer = new OutputStreamWriter(os)) {
                template.process(data, writer);
            } catch (TemplateException e) {
                e.printStackTrace();
                ResponseCodes.sendErrorResponse(exchange, ResponseCodes.INTERNAL_SERVER_ERROR, "Ошибка при обработке шаблона: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseCodes.sendErrorResponse(exchange, ResponseCodes.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}
