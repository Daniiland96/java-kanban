package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void createTaskManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    void managersCreateWorkerTaskManager() {
        Task task = new Task("model.Task", "model.Task", Status.NEW);
        taskManager.createTask(task);
        taskManager.getAnyTaskById(1);
        assertEquals(taskManager.tasks.size(), 1); // в tasks должен появиться 1 элемент
        assertEquals(taskManager.getHistory().size(), 1); // в списке просмотренных задач должен появиться 1 элемент
    }

}