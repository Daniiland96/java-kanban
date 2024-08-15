package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {
    File taskFile = null;

    @Override
    @BeforeEach
    void createTaskManagerTest() {
        try {
            taskFile = File.createTempFile("newFile", ".csv");
            taskManager = FileBackedTaskManager.loadFromFile(taskFile);
            task = new Task("Task", "TaskType", Status.NEW);
            epic = new Epic("Epic", "EpicType");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания временного файла.");
        }
    }

    @Test
    public void loadEmptyFile() {
        ArrayList<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void saveToFile() throws IOException {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        String fileString = Files.readString(taskFile.toPath());
        String str = "id,type,name,status,description,epic\n1,TASK,Task,NEW,TaskType\n2,EPIC,Epic,NEW,EpicType,empty\n";
        assertEquals(str, fileString);
    }

    @Test
    public void loadFromFile() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(taskFile);
        assertEquals(taskManager.getAnyTaskById(1), taskManager2.getAnyTaskById(1));
        assertEquals(taskManager.getAnyTaskById(2), taskManager2.getAnyTaskById(2));
    }
}
