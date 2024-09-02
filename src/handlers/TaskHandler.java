package handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import model.TypeTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (path.equals("/tasks")) {
                    String allTasks = gson.toJson(manager.getAllTask());
                    sendText(exchange, allTasks, 200);
                } else if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                    String task;
                    int id = checkId(pathSplit[2]);
                    if (manager.getAnyTaskById(id).getType() == TypeTask.TASK) {
                        task = gson.toJson(manager.getAnyTaskById(id));
                        sendText(exchange, task, 200);
                    } else {
                        task = gson.toJson("Под id: " + id + " задана задача типа "
                                + manager.getAnyTaskById(id).getType());
                        sendNotFound(exchange, task);
                    }
                } else {
                    sendNotFound(exchange, gson.toJson("Запрос не найден."));
                }
                break;
            case "POST":
                if (path.equals("/tasks")) {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    manager.createTask(task);
                    sendText(exchange, gson.toJson("Task успешно создан."), 201);
                } else if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                    int id = checkId(pathSplit[2]);
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    manager.updateTask(id, task);
                    sendText(exchange, gson.toJson("Task успешно обновлен."), 201);
                } else {
                    sendNotFound(exchange, gson.toJson("Запрос не найден."));
                }
                break;
            case "DELETE":
                if (pathSplit[1].equals("tasks") && pathSplit.length == 3) {
                    int id = checkId(pathSplit[2]);
                    if (manager.getAnyTaskById(id).getType() == TypeTask.TASK) {
                        manager.deleteAnyTaskById(id);
                        sendText(exchange, gson.toJson("Задача успешно удалена."), 200);
                    } else {
                        sendNotFound(exchange, gson.toJson("Под id: " + id + " задана задача типа "
                                + manager.getAnyTaskById(id).getType()));
                    }
                } else {
                    sendNotFound(exchange, gson.toJson("Запрос не найден."));
                }
                break;
            default:
                sendNotFound(exchange, gson.toJson("Запрос не найден."));
        }
    }
}
