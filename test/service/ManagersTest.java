package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    InMemoryTaskManager taskManager;
    FileBackedTaskManager backedTaskManager;
    File taskFile = null;

    @BeforeEach
    void createTaskManager() {
        try {
            taskFile = File.createTempFile("newFile", ".csv");
            taskManager = (InMemoryTaskManager) Managers.getDefault();
            backedTaskManager = (FileBackedTaskManager) Managers.getDefault(taskFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания временного файла.");
        }
    }

    @Test
    void managersCreateWorkerTaskManager() {
        Task task = new Task("Task", "Task", Status.NEW);
        taskManager.createTask(task);
        taskManager.getAnyTaskById(1);
        assertEquals(taskManager.tasks.size(), 1); // в tasks должен появиться 1 элемент
        assertEquals(taskManager.getHistory().size(), 1); // в списке просмотренных задач должен появиться 1 элемент
    }

    @Test
    void managersCreateWorkerBackedTaskManager() {
        Task task = new Task("Task", "Task", Status.NEW);
        backedTaskManager.createTask(task);
        backedTaskManager.getAnyTaskById(1);
        assertEquals(backedTaskManager.tasks.size(), 1); // в tasks должен появиться 1 элемент
        assertEquals(backedTaskManager.getHistory().size(), 1); // в истории задач должен появиться 1 элемент
    }
}