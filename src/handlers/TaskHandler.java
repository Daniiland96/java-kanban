package handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import model.TypeTask;
import service.NotFoundException;
import service.TaskManager;
import service.TimeIntersectingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (path.equals("/tasks")) {
                        String allTasks = gson.toJson(manager.getAllTask());
                        sendText(exchange, allTasks, 200);
                    }
                    if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                        String task;
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.TASK) {
                            task = gson.toJson(manager.getAnyTaskById(id));
                            sendText(exchange, task, 200);
                        } else {
                            task = gson.toJson("Под id: " + id + "задана задача типа "
                                    + manager.getAnyTaskById(id).getType());
                            sendNotFound(exchange, task);
                        }
                    } else sendNotFound(exchange, gson.toJson("Запрос не найден."));
                } catch (NotFoundException e) {
                    sendNotFound(exchange, gson.toJson(e.getMessage()));
                } catch (TimeIntersectingException e) {
                    sendHasInteractions(exchange, gson.toJson(e.getMessage()));
                }
                break;
            case "POST":
                try {
                    if (path.equals("/tasks")) {
                        try {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(body, Task.class);
                            manager.createTask(task);
                            sendText(exchange, gson.toJson("Task успешно создан."), 201);
                        } catch (IOException e) {
                            sendNotFound(exchange, gson.toJson("Ошибка при создании Task."));
                        }
                    }
                    if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                        try {
                            int id = checkId(pathSplit[2]);
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(body, Task.class);
                            manager.updateTask(id, task);
                            sendText(exchange, gson.toJson("Task успешно обновлен."), 201);
                        } catch (IOException e) {
                            sendNotFound(exchange, gson.toJson("Ошибка при создании Task."));
                        }
                    } else sendNotFound(exchange, gson.toJson("Запрос не найден."));
                } catch (NotFoundException e) {
                    sendNotFound(exchange, gson.toJson(e.getMessage()));
                } catch (TimeIntersectingException e) {
                    sendHasInteractions(exchange, gson.toJson(e.getMessage()));
                }
                break;
            case "DELETE":
                try {
                    if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.TASK) {
                            manager.deleteAnyTaskById(id);
                            sendText(exchange, gson.toJson("Задача успешно удалена."), 200);
                        } else {
                            sendNotFound(exchange, gson.toJson("Под id: " + id + "задана задача типа "
                                    + manager.getAnyTaskById(id).getType()));
                        }
                    } else sendNotFound(exchange, gson.toJson("Запрос не найден."));
                } catch (NotFoundException e) {
                    sendNotFound(exchange, gson.toJson(e.getMessage()));
                } catch (TimeIntersectingException e) {
                    sendHasInteractions(exchange, gson.toJson(e.getMessage()));
                }
                break;
            default: sendNotFound(exchange, gson.toJson("Запрос не найден."));
        }
    }
}
