package handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.TypeTask;
import service.NotFoundException;
import service.TaskManager;
import service.TimeIntersectingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET":
                try {
                    if (path.equals("/epics")) {
                        String allEpics = gson.toJson(manager.getAllEpic());
                        sendText(exchange, allEpics, 200);
                    }
                    if (pathSplit[1].equals("epics") && pathSplit.length == 3) {
                        String task;
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.EPIC) {
                            task = gson.toJson(manager.getAnyTaskById(id));
                            sendText(exchange, task, 200);
                        } else {
                            task = gson.toJson("Под id: " + id + " задана задача типа "
                                    + manager.getAnyTaskById(id).getType());
                            sendNotFound(exchange, task);
                        }
                    }
                    if (pathSplit[1].equals("epics") && pathSplit.length == 4 && pathSplit[3].equals("subtasks")) {
                        String task;
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.EPIC) {
                            task = gson.toJson(manager.getEpicsSubtasks(id));
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
                    if (path.equals("/epics")) {
                        try {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Epic epic = gson.fromJson(body, Epic.class);
                            manager.createEpic(epic);
                            sendText(exchange, gson.toJson("Epic успешно создан."), 201);
                        } catch (IOException e) {
                            sendNotFound(exchange, gson.toJson("Ошибка при создании Epic."));
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
                    if (pathSplit[1].equals("epics") && pathSplit.length == 3) {
                        int id = checkId(pathSplit[2]);
                        if (manager.getAnyTaskById(id).getType() == TypeTask.EPIC) {
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
