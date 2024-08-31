package handlers;

import adaptersAndTokens.DurationAdapter;
import adaptersAndTokens.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;
    protected Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).serializeNulls().setPrettyPrinting().create();

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    public abstract void handle(HttpExchange exchange) throws IOException;

    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {

        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {

        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(404, 0);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {

        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(406, 0);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected int checkId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}