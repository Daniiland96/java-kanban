package server;

import adaptersAndTokens.DurationAdapter;
import adaptersAndTokens.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import handlers.TaskHandler;
import model.Status;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        manager.createTask(new Task("T", "D", Status.DONE));
        manager.createTask(new Task("T2", "D2", Status.IN_PROGRESS, "20.08.24 10:00", 60));

        Task task3 = new Task("T3", "D3", Status.NEW);
        Task task4 = new Task("T4", "D4", Status.NEW, "20.08.24 11:00", 60);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
        server.createContext("/tasks", new TaskHandler(manager));
        server.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

        HttpClient client = HttpClient.newHttpClient();
//        URI uri = URI.create("http://localhost:8080/tasks");
        URI uri = URI.create("http://localhost:8080/tasks/1");

        String gsonTask3 = gson.toJson(task4);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .GET()
//                .POST(HttpRequest.BodyPublishers.ofString(gsonTask3))
//                .DELETE()
                .header("Accept", "application/json")
                .build();
        try {
            System.out.println(manager.getAllTask());
            System.out.println();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            System.out.println(jsonElement);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(manager.getAllTask());
        server.stop(1);
    }
}
