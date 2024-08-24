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
        task1 = new Task("Task1", "t1", Status.NEW, "20.08.24 10:00", 60);
        epic2 = new Epic("Epic2", "e2");
        subtask2Id3 = new Subtask("Subtask2_3", "s2_3", Status.IN_PROGRESS,
                "20.08.24 11:00", 60);
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
        assertEquals(manager.getHistory().size(), 0);
        manager.getAnyTaskById(task1.id);
        Task task = manager.getHistory().getFirst();
        assertEquals(task.title, "Task1");

        manager.updateTask(task1.id, new Task("newTask", "new", Status.DONE,
                "25.08.24 12:00", 30));
        task = manager.getHistory().getFirst();
        assertEquals(task.title, "Task1");

        manager.getAnyTaskById(task1.id);
        task = manager.getHistory().getFirst();
        assertEquals(task.title, "newTask");
    }

    @Test
    void removeFromEndsAndMiddle() {
        Epic epic4 = new Epic("Epic4", "4");
        Epic epic5 = new Epic("Epic5", "5");
        manager.createEpic(epic4);
        manager.createEpic(epic5);
        manager.getAnyTaskById(task1.id);
        manager.getAnyTaskById(epic2.id);
        manager.getAnyTaskById(subtask2Id3.id);
        manager.getAnyTaskById(epic4.id);
        manager.getAnyTaskById(epic5.id);
        assertEquals(manager.getHistory().getFirst(), task1);
        assertEquals(manager.getHistory().getLast(), epic5);
        assertEquals(manager.getHistory().get(subtask2Id3.id - 1), subtask2Id3);
        manager.removeTaskFromHistory(task1.id);
        manager.removeTaskFromHistory(epic5.id);
        manager.removeTaskFromHistory(subtask2Id3.id);
        assertEquals(manager.getHistory().getFirst(), epic2);
        assertEquals(manager.getHistory().getLast(), epic4);
    }
}
