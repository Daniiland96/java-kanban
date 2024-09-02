package server;

import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import tokens.ListEpicTypeToken;
import tokens.ListSubtaskTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EpicServerTest extends BaseServerTest {

    @Test
    void getEpicsTest() {
        try {
            manager.createEpic(epic1);
            manager.createEpic(epic2);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/epics");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Epic> array = gson.fromJson(response.body(), new ListEpicTypeToken().getType());
            assertEquals(array.size(), 2);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void getEpicByIdTest() {
        try {
            manager.createEpic(epic1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/epics/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Epic epic = gson.fromJson(response.body(), Epic.class);
            assertEquals(epic, manager.getAnyTaskById(epic1.getId()));
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void getEpicSubtasksTest() {
        try {
            manager.createEpic(epic1);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/epics/1/subtasks");

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
    void postNewEpicTest() {
        try {
            String gsonEpic = gson.toJson(epic1);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/epics");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gsonEpic))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllEpic().size(), 1);
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }

    @Test
    void deleteEpicTest() {
        try {
            manager.createEpic(epic1);
            manager.createEpic(epic2);
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/epics/1");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(manager.getAllEpic().size(), 1);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }
}
