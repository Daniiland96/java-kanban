package handlers;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

import static server.HttpTaskServer.gson;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (path.equals("/history")) {
                    String history = gson.toJson(manager.getHistory());
                    sendText(exchange, history, 200);
                } else sendNotFound(exchange, gson.toJson("Запрос не найден."));
                break;
            default:
                sendNotFound(exchange, gson.toJson("Запрос не найден."));
        }
    }
}
