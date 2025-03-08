package main.java.server;

import com.sun.net.httpserver.HttpServer;
import freemarker.template.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
//        cfg.setClassForTemplateLoading(Main.class, "ftl/");
        cfg.setDirectoryForTemplateLoading(new File("data/ftl"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);

        TaskManager taskManager = new TaskManager();

        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);

        server.createContext("/", new CalendarPageHandler(cfg, taskManager));
        server.createContext("/tasks", new TaskListHandler(cfg, taskManager));
        server.createContext("/task/add", new TaskAddHandler(cfg, taskManager));
        server.createContext("/task/delete", new TaskDeleteHandler(cfg, taskManager));
        server.createContext("/task/edit", new TaskEditHandler(cfg, taskManager));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8090...");
        System.out.println("Open in browser: http://localhost:8090/");
    }
}
