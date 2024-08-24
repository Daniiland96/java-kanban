package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    void createTaskManagerTest() {
        super.createTaskManagerTest();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void epicCannotBeAddItself() {
        taskManager.createEpic(epic);
        // taskManager.createSubtask(epic.id, (model.Subtask) epic); // нельзя создать model.Subtask типа model.Epic
        assertEquals(taskManager.subtasks.size(), 0);
    }

    @Test
    void canNotMakeSubtaskTheEpic() {
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskFirst", Status.NEW,
                "21.08.24 10:00", 60);
        taskManager.createSubtask(epic.id, subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "SubtaskSecond", Status.NEW,
                "22.08.24 10:00", 60);
        taskManager.createSubtask(subtask2.id, subtask2); // в консоль выведет сообщение "model.Epic,
        // с указанным id, не найден."
        assertEquals(taskManager.subtasks.size(), 1); // в subtasks должен быть только subtask1
    }

    @Test
    void taskManagerCreateAllTypeTaskAndReturnById() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, subtask);
        assertEquals(taskManager.getAnyTaskById(task.id).typeTask, TypeTask.TASK);
        assertEquals(taskManager.getAnyTaskById(epic.id).typeTask, TypeTask.EPIC);
        assertEquals(taskManager.getAnyTaskById(subtask.id).typeTask, TypeTask.SUBTASK);
    }

    @Test
    void createdIdDoNotConflictWithGenerated() {
        int id = 5;
        task.id = id;
        taskManager.createTask(task);
        assertNotEquals(taskManager.getAnyTaskById(task.id).id, id); // id изменился, значит конфликта нет
    }

    @Test
    void taskManagerDoesNotChangeFields() {
        taskManager.createTask(task);
        assertEquals(taskManager.getAnyTaskById(task.id).title, "Task");
        assertEquals(taskManager.getAnyTaskById(task.id).description, "TaskType");
        assertEquals(taskManager.getAnyTaskById(task.id).status, Status.NEW);
    }

    @Test
    void epicRemoveSubtaskIdWhichWasDeleted() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, subtask);
        assertEquals(epic.getArraySubtask().size(), 1);
        taskManager.deleteAnyTaskById(subtask.id);
        assertEquals(epic.getArraySubtask().size(), 0);
    }

    @Test
    void getEpicsSubtasksTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, new Subtask("Subtask1", "s1", Status.IN_PROGRESS,
                "20.08.24 11:00", 60));
        taskManager.createSubtask(epic.id, new Subtask("Subtask2", "s2", Status.DONE,
                "20.08.24 12:00", 60)); // id 3
        taskManager.createSubtask(epic.id, new Subtask("Subtask3", "s3", Status.NEW,
                "20.08.24 13:00", 60));
        taskManager.deleteAnyTaskById(3);
        assertEquals(taskManager.getEpicsSubtasks(epic.id).size(), 2);
    }

    @Test
    void subtaskHaveEpicId() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, subtask);
        assertEquals(epic.id, subtask.getEpicId());
    }

    @Test
    void epicUpdateStatusTest() {
        taskManager.createEpic(epic);
        assertEquals(epic.status, Status.NEW);

        taskManager.createSubtask(epic.id, subtask);
        assertEquals(epic.status, Status.NEW);

        taskManager.updateSubtask(subtask.id, new Subtask("Subtask", "SubtaskType", Status.IN_PROGRESS,
                "20.08.24 11:00", 60));
        assertEquals(epic.status, Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask.id, new Subtask("Subtask", "SubtaskType", Status.DONE,
                "20.08.24 11:00", 60));
        assertEquals(epic.status, Status.DONE);

        taskManager.createSubtask(epic.id, new Subtask("Subtask2", "SubtaskSecond", Status.NEW,
                "22.08.24 10:00", 60));
        assertEquals(epic.status, Status.IN_PROGRESS);
    }

    @Test
    void intersectionOfDateTimeTest() {
        taskManager.createTask(task);
        assertEquals(taskManager.getPrioritizedTasks().size(), 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.id, subtask);
        assertEquals(taskManager.getPrioritizedTasks().size(), 2);
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:30",
                60));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 11:30",
                60));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:00",
                240));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:30",
                60));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00",
                10));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 11:30",
                30));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:10",
                10));
        taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:30",
                60));
        assertEquals(taskManager.getPrioritizedTasks().size(), 2);
    }
}
