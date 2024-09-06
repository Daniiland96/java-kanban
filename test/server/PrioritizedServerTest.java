
package server;

import model.Task;
import org.junit.jupiter.api.Test;
import tokens.ListTaskTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedServerTest extends BaseServerTest {

    @Test
    void getPrioritizedTaskTest() {
        try {
            manager.createTask(task1);
            manager.createTask(task2);
            manager.createEpic(epic1);
            manager.createEpic(epic2);
            manager.createSubtask(epic1.getId(), subtask1);
            manager.createSubtask(epic1.getId(), subtask2);

            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8080/prioritized");

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(uri)
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> array = gson.fromJson(response.body(), new ListTaskTypeToken().getType());
            assertEquals(array.size(), 4);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка формирования запроса.");
        }
    }
}
