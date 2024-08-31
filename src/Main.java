import adaptersAndTokens.DurationAdapter;
import adaptersAndTokens.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;

import service.*;

import java.io.File;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        File file = new File("manager.csv");

        TaskManager backedTaskManager = Managers.getDefault();

        Task task = new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00", 60);
        Epic epic = new Epic("Epic", "EpicType");
        Subtask subtask = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 11:00", 60);
        Task task1 = new Task("AAA", "AAA", Status.IN_PROGRESS);
        Task task2 = new Task("BBB", "BBB", Status.DONE);
        Subtask subtask1 = new Subtask("CCC", "CCC", Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 13:00", 60);

        backedTaskManager.createTask(task1);
        backedTaskManager.createTask(task2);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.getId(), subtask2);
        backedTaskManager.createSubtask(epic.getId(), subtask1);
        backedTaskManager.createSubtask(epic.getId(), subtask);
        backedTaskManager.createTask(task);


        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).serializeNulls().setPrettyPrinting().create();

        System.out.println(epic);
        System.out.println();

        String st = gson.toJson(epic);

        System.out.println(st);
        System.out.println();

        Task newTask = gson.fromJson(st, Epic.class);

        System.out.println(newTask);

        //                            Task task = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8)
//                                    , Task.class);
        //                            String str = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);
//                            Task task = gson.fromJson(str, Task.class);
//                            JsonElement jsonElement = JsonParser.parseString(str);
//                            Task task = gson.fromJson(jsonElement, Task.class);
//                            if (task == null) sendNotFound(exchange, gson.toJson("Неверно указаны параметры Task."));
    }
}
