package handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.TypeTask;
import service.NotFoundException;
import service.TaskManager;
import service.TimeIntersectingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (path.equals("/subtasks")) {
                        String allSubtasks = gson.toJson(manager.getAllSubtask());
                        sendText(exchange, allSubtasks, 200);
                    }
                    if (pathSplit[1].equals("subtasks") && pathSplit.length == 3) {
                        String task;
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.SUBTASK) {
                            task = gson.toJson(manager.getAnyTaskById(id));
                            sendText(exchange, task, 200);
                        } else {
                            task = gson.toJson("Под id: " + id + " задана задача типа "
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
                    if (pathSplit.length == 4 && pathSplit[1].equals("subtasks") && pathSplit[2].equals("epic")) {
                        try {
                            int epicId = checkId(pathSplit[3]);
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            manager.createSubtask(epicId, subtask);
                            sendText(exchange, gson.toJson("Subtask успешно создан в Epic " + epicId), 201);
                        } catch (IOException e) {
                            sendNotFound(exchange, gson.toJson("Ошибка при создании Subtask."));
                        }
                    }
                    if (pathSplit[1].equals("subtasks") && pathSplit.length == 3) {
                        try {
                            int id = checkId(pathSplit[2]);
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            manager.updateSubtask(id, subtask);
                            sendText(exchange, gson.toJson("Subtask успешно обновлен."), 201);
                        } catch (IOException e) {
                            sendNotFound(exchange, gson.toJson("Ошибка при создании Subtask."));
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
                    if (pathSplit[1].equals("subtasks") && pathSplit.length == 3) {
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.SUBTASK) {
                            manager.deleteAnyTaskById(id);
                            sendText(exchange, gson.toJson("Задача успешно удалена."), 200);
                        } else {
                            sendNotFound(exchange, gson.toJson("Под id: " + id + " задана задача типа "
                                    + manager.getAnyTaskById(id).getType()));
                        }
                    } else sendNotFound(exchange, gson.toJson("Запрос не найден."));
                } catch (NotFoundException e) {
                    sendNotFound(exchange, gson.toJson(e.getMessage()));
                } catch (TimeIntersectingException e) {
                    sendHasInteractions(exchange, gson.toJson(e.getMessage()));
                }
                break;
            default:
                sendNotFound(exchange, gson.toJson("Запрос не найден."));
        }
    }
}