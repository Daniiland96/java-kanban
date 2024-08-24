package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {
    File taskFile = null;

    @Override
    @BeforeEach
    void createTaskManagerTest() {
        super.createTaskManagerTest();
        try {
            taskFile = File.createTempFile("newFile", ".csv");
            taskManager = FileBackedTaskManager.loadFromFile(taskFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания временного файла.");
        }
    }

    @Test
    void loadEmptyFileTest() {
        ArrayList<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    void saveToFileTest() throws IOException {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, subtask);
        String fileString = Files.readString(taskFile.toPath());
        String str = "id,type,name,status,description,epic,startTime,endTime,duration\n" +
                "1,TASK,Task,NEW,TaskType,20.08.24 10:00,20.08.24 11:00,60\n" +
                "2,EPIC,Epic,NEW,EpicType,3,20.08.24 11:00,20.08.24 12:00,60\n" +
                "3,SUBTASK,Subtask,NEW,SubtaskType,2,20.08.24 11:00,20.08.24 12:00,60\n";
        assertEquals(str, fileString);
    }

    @Test
    void loadFromFileTest() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(taskFile);
        assertEquals(taskManager.getAnyTaskById(task.id), taskManager2.getAnyTaskById(task.id));
        assertEquals(taskManager.getAnyTaskById(epic.id), taskManager2.getAnyTaskById(epic.id));
    }

    @Test
    void fileBackedTaskManagerExceptionTest() {
        File notExistFile = new File("");
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(notExistFile));
    }

    @Test
    void numberFormatExceptionTest() {
        try (FileWriter writer = new FileWriter(taskFile, StandardCharsets.UTF_8)) {
            writer.write("Неверный текст" + "\n" + "Неверный текст");
        } catch (NumberFormatException | IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        }
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(taskFile));
    }
}
