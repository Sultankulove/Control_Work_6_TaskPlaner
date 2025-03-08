package main.java.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ResponseCodes {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    REDIRECT(303, "Redirect");

    private static final Logger logger = Logger.getLogger(ResponseCodes.class.getName());

    private final int code;
    private final String description;

    ResponseCodes(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }

    public static void sendErrorResponse(HttpExchange exchange, ResponseCodes responseCode, String message) {
        try {
            String htmlResponse = "<html><head><meta charset='utf-8'><title>" +
                    responseCode.getCode() + " " + responseCode.getDescription() +
                    "</title></head><body><h1>" + responseCode.getDescription() +
                    "</h1><p>" + message + "</p></body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(responseCode.getCode(), htmlResponse.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(htmlResponse.getBytes());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при отправке ответа", e);
        }
    }
}
