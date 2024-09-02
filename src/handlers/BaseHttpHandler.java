package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.HttpTaskServer;
import service.NotFoundException;
import service.TaskManager;
import service.TimeIntersectingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;
    protected Gson gson = HttpTaskServer.getGson();
    String path;
    String[] pathSplit;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    public void handle(HttpExchange exchange) throws IOException {
        path = exchange.getRequestURI().getPath();
        pathSplit = path.split("/");
        try {
            safeHandle(exchange);
        } catch (NotFoundException e) {
            sendNotFound(exchange, gson.toJson(e.getMessage()));
        } catch (TimeIntersectingException e) {
            sendHasInteractions(exchange, gson.toJson(e.getMessage()));
        }
    }

    public abstract void safeHandle(HttpExchange exchange) throws IOException;

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