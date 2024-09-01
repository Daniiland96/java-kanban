package server;

import tokens.ListTaskTypeToken;
import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static server.HttpTaskServer.gson;

public class TaskServerTest extends BaseServerTest {

    @Test
    void getTasksTest() {
        try {
            manager.createTask(task1);
            manager.createTask(task2);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/tasks");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> array = gson.fromJson(response.body(), new ListTaskTypeToken().getType());
            assertEquals(array.size(), 2);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void getTaskByIdTest() {
        try {
            manager.createTask(task1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/tasks/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task task = gson.fromJson(response.body(), Task.class);
            assertEquals(task, manager.getAnyTaskById(task1.getId()));
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void postNewTaskTest() {
        try {
            String gsonTask = gson.toJson(task1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/tasks");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllTasks().size(), 1);
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void postUpdateTaskTest() {
        try {
            manager.createTask(task1);
            String gsonTask = gson.toJson(task2);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/tasks/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAnyTaskById(task1.getId()).getTitle(), "T2");
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void deleteTaskTest() {
        try {
            manager.createTask(task1);
            manager.createTask(task2);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/tasks/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllTasks().size(), 1);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }
}
