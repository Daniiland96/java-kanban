package server;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().create();
    private static HttpServer server;
    private static TaskManager manager;

    public static void main(String[] args) throws IOException, InterruptedException {
        startServer();
        stopServer();
    }

    public static void startServer() {
        try {
            manager = Managers.getDefault();
            server = HttpServer.create(new InetSocketAddress(PORT), 0);

            server.createContext("/tasks", new TaskHandler(manager));
            server.createContext("/epics", new EpicHandler(manager));
            server.createContext("/subtasks", new SubtaskHandler(manager));
            server.createContext("/history", new HistoryHandler(manager));
            server.createContext("/prioritized", new PrioritizedHandler(manager));

            server.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        server.stop(1);
    }

    public static Gson getGson() {
        return gson;
    }

    public static TaskManager getManager() {
        return manager;
    }
}
