package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    TaskManager manager;
    Task task1;
    Epic epic2;
    Subtask subtask2Id3;

    @BeforeEach
    void CreateInMemoryHistoryManagerTest() {
        manager = Managers.getDefault();

        task1 = new Task("Task1", "t1", Status.NEW);
        epic2 = new Epic("Epic2", "e2");
        subtask2Id3 = new Subtask("Subtask2_3", "s2_3", Status.IN_PROGRESS);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubtask(epic2.id, subtask2Id3);
    }

    @Test
    void historyManagerDoesNotChangeFields() {
        manager.getAnyTaskById(1);
        Task historyTask = manager.getHistory().getFirst();
        assertEquals(task1.id, historyTask.id);
        assertEquals(task1.title, historyTask.title);
        assertEquals(task1.description, historyTask.description);
        assertEquals(task1.status, historyTask.status);
    }

    @Test
    void historyManagerCreateListWithUniqueNodes() {
        manager.getAnyTaskById(task1.id);
        manager.getAnyTaskById(epic2.id);
        manager.getAnyTaskById(subtask2Id3.id);
        manager.getAnyTaskById(task1.id);
        assertEquals(manager.getHistory().size(), 3);
        assertEquals(task1, manager.getHistory().getLast());
    }

    @Test
    void historyManagerRemoveNodesFromList() {
        manager.getAnyTaskById(task1.id);
        manager.getAnyTaskById(epic2.id);
        manager.getAnyTaskById(subtask2Id3.id);
        manager.removeTaskFromHistory(task1.id);
        manager.removeTaskFromHistory(subtask2Id3.id);
        assertEquals(manager.getHistory().size(), 1);
    }

    @Test
    void ifDeleteTaskWillBeDeletedHistory() {
        manager.getAnyTaskById(task1.id);
        manager.getAnyTaskById(epic2.id);
        manager.getAnyTaskById(subtask2Id3.id);
        assertEquals(manager.getHistory().size(), 3);
        manager.deleteAllTasks();
        assertEquals(manager.getHistory().size(), 0);
    }

    @Test
    void updateTaskInHistoryList() {
        manager.getAnyTaskById(task1.id);
        Task task = manager.getHistory().getFirst();
        assertEquals(task.title, "Task1");

        manager.updateTask(task1.id, new Task("newTask", "new", Status.DONE));
        task = manager.getHistory().getFirst();
        assertEquals(task.title, "Task1");

        manager.getAnyTaskById(task1.id);
        task = manager.getHistory().getFirst();
        assertEquals(task.title, "newTask");
    }
}
