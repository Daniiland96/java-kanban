
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

public class HistoryServerTest extends BaseServerTest {

    @Test
    void getHistoryTest() {
        try {
            manager.createTask(task1);
            manager.createTask(task2);
            manager.createEpic(epic1);
            manager.createEpic(epic2);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);

            manager.getAnyTaskById(task1.getId());
            manager.getAnyTaskById(task2.getId());
            manager.getAnyTaskById(epic1.getId());
            manager.getAnyTaskById(epic2.getId());
            manager.getAnyTaskById(subtask1.getId());
            manager.getAnyTaskById(subtask2.getId());
            manager.getAnyTaskById(task1.getId());

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/history");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> array = gson.fromJson(response.body(), new ListTaskTypeToken().getType());
            assertEquals(array.size(), 6);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }
}
