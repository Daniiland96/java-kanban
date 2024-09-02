package server;

import model.Subtask;
import org.junit.jupiter.api.Test;
import tokens.ListSubtaskTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskServerTest extends BaseServerTest {

    @Test
    void getSubtasksTest() {
        try {
            manager.createEpic(epic1);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/subtasks");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Subtask> array = gson.fromJson(response.body(), new ListSubtaskTypeToken().getType());
            assertEquals(array.size(), 2);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void getSubtaskByIdTest() {
        try {
            manager.createEpic(epic1);
            manager.createSubtask(epic1.getId(), subtask1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/subtasks/2");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Subtask subtask = gson.fromJson(response.body(), Subtask.class);
            assertEquals(subtask, manager.getAnyTaskById(subtask1.getId()));
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void postNewSubtaskTest() {
        try {
            manager.createEpic(epic1);
            String gsonSubtask = gson.toJson(subtask1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/subtasks/epic/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllSubtask().size(), 1);
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void postUpdateSubtaskTest() {
        try {
            manager.createEpic(epic1);
            manager.createSubtask(epic1.getId(), subtask1);
            String gsonSubtask = gson.toJson(subtask2);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/subtasks/2");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gsonSubtask))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAnyTaskById(subtask1.getId()).getTitle(), "S2");
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void deleteSubtaskTest() {
        try {
            manager.createEpic(epic1);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/subtasks/2");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllSubtask().size(), 1);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }
}
