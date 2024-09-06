package handlers;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (path.equals("/prioritized")) {
                    String prioritized = gson.toJson(manager.getPrioritizedTasks());
                    sendText(exchange, prioritized, 200);
                } else {
                    sendNotFound(exchange, gson.toJson("Запрос не найден."));
                }
                break;
            default:
                sendNotFound(exchange, gson.toJson("Запрос не найден."));
        }
    }
}