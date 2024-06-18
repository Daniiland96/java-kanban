import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;
    Task task;
    Epic epic;

    @BeforeEach
    void CreateInMemoryTaskManagerTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        task = new Task("Task", "TaskType", Status.NEW);
        epic = new Epic("Epic", "EpicType");
    }

    @Test
    void epicCannotBeAddItself() {
        taskManager.createEpic(epic);
        // taskManager.createSubtask(epic.id, (Subtask) epic); // нельзя создать Subtask типа Epic
        assertEquals(taskManager.subtasks.size(), 0); //
    }

    @Test
    void canNotMakeSubtaskTheEpic() {
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskFirst", Status.NEW);
        taskManager.createSubtask(1, subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "SubtaskSecond", Status.NEW);
        taskManager.createSubtask(2, subtask2); // в консоль выведет сообщение "Epic, с указанным id, не найден."
        assertEquals(taskManager.subtasks.size(), 1); // в subtasks должен быть только subtask1
    }

    @Test
    void taskManagerCreateAllTypeTaskAndReturnById() {
        Subtask subtask = new Subtask("Subtask", "SubtaskType", Status.NEW);
        taskManager.createTask(task); // id = 1
        taskManager.createEpic(epic); // id = 2
        taskManager.createSubtask(2, subtask); // id = 3
        assertEquals(taskManager.getAnyTaskById(1).title, "Task");
        assertEquals(taskManager.getAnyTaskById(2).title, "Epic");
        assertEquals(taskManager.getAnyTaskById(3).title, "Subtask");
    }

    @Test
    void createdIdDoNotConflictWithGenerated() {
        task.id = 5;
        taskManager.createTask(task);
        assertNotEquals(taskManager.getAnyTaskById(1).id, 5); // id изменился, значит конфликта нет
    }

    @Test
    void taskManagerDoesNotChangeFields() {
        taskManager.createTask(task);
        assertEquals(taskManager.getAnyTaskById(1).title, "Task");
        assertEquals(taskManager.getAnyTaskById(1).description, "TaskType");
        assertEquals(taskManager.getAnyTaskById(1).status, Status.NEW);
    }

    @Test
    void historyManagerDoesNotChangeFields(){
        taskManager.createTask(task);
        taskManager.getAnyTaskById(1);
        Task historyTask = taskManager.getHistory().getFirst();
        assertEquals(task.id, historyTask.id);
        assertEquals(task.title, historyTask.title);
        assertEquals(task.description, historyTask.description);
        assertEquals(task.status, historyTask.status);
    }
}