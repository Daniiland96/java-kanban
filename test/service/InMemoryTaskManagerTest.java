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
        // taskManager.createSubtask(epic.id, epic); // нельзя создать Subtask типа Epic
        assertEquals(taskManager.subtasks.size(), 0);
    }

    @Test
    void canNotMakeSubtaskTheEpic() {
        try {
            taskManager.createEpic(epic);
            Subtask subtask1 = new Subtask("Subtask1", "SubtaskFirst", Status.NEW,
                    "21.08.24 10:00", 60);
            taskManager.createSubtask(epic.getId(), subtask1);
            Subtask subtask2 = new Subtask("Subtask2", "SubtaskSecond", Status.NEW,
                    "22.08.24 10:00", 60);
            taskManager.createSubtask(subtask2.getId(), subtask2);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(taskManager.subtasks.size(), 1); // в subtasks должен быть только subtask1
    }

    @Test
    void taskManagerCreateAllTypeTaskAndReturnById() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(taskManager.getAnyTaskById(task.getId()).getType(), TypeTask.TASK);
        assertEquals(taskManager.getAnyTaskById(epic.getId()).getType(), TypeTask.EPIC);
        assertEquals(taskManager.getAnyTaskById(subtask.getId()).getType(), TypeTask.SUBTASK);
    }

    @Test
    void createdIdDoNotConflictWithGenerated() {
        int id = 5;
        task.setId(id);
        taskManager.createTask(task);
        assertNotEquals(taskManager.getAnyTaskById(task.getId()).getId(), id); // id изменился, значит конфликта нет
    }

    @Test
    void taskManagerDoesNotChangeFields() {
        taskManager.createTask(task);
        assertEquals(taskManager.getAnyTaskById(task.getId()).getTitle(), "Task");
        assertEquals(taskManager.getAnyTaskById(task.getId()).getDescription(), "TaskType");
        assertEquals(taskManager.getAnyTaskById(task.getId()).getStatus(), Status.NEW);
    }

    @Test
    void epicRemoveSubtaskIdWhichWasDeleted() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(epic.getArraySubtask().size(), 1);
        taskManager.deleteAnyTaskById(subtask.getId());
        assertEquals(epic.getArraySubtask().size(), 0);
    }

    @Test
    void getEpicsSubtasksTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), new Subtask("Subtask1", "s1", Status.IN_PROGRESS,
                "20.08.24 11:00", 60));
        taskManager.createSubtask(epic.getId(), new Subtask("Subtask2", "s2", Status.DONE,
                "20.08.24 12:00", 60)); // id 3
        taskManager.createSubtask(epic.getId(), new Subtask("Subtask3", "s3", Status.NEW,
                "20.08.24 13:00", 60));
        taskManager.deleteAnyTaskById(3);
        assertEquals(taskManager.getEpicsSubtasks(epic.getId()).size(), 2);
    }

    @Test
    void subtaskHaveEpicId() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(epic.getId(), subtask.getEpicId());
    }

    @Test
    void epicUpdateStatusTest() {
        taskManager.createEpic(epic);
        assertEquals(epic.getStatus(), Status.NEW);

        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(epic.getStatus(), Status.NEW);

        taskManager.updateSubtask(subtask.getId(), new Subtask("Subtask", "SubtaskType", Status.IN_PROGRESS,
                "20.08.24 11:00", 60));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask.getId(), new Subtask("Subtask", "SubtaskType", Status.DONE,
                "20.08.24 11:00", 60));
        assertEquals(epic.getStatus(), Status.DONE);

        taskManager.createSubtask(epic.getId(), new Subtask("Subtask2", "SubtaskSecond", Status.NEW,
                "22.08.24 10:00", 60));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void intersectionOfDateTimeTest() {
        taskManager.createTask(task);
        assertEquals(taskManager.getPrioritizedTasks().size(), 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(taskManager.getPrioritizedTasks().size(), 2);
        taskManager.createSubtask(epic.getId(), new Subtask("Subtask", "SubtaskType", Status.IN_PROGRESS));
        // size = 3
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:30",
                    60));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 11:30",
                    60));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createSubtask(epic.getId(), new Subtask("Subtask", "SubtaskType", Status.IN_PROGRESS));
            // size = 4
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:00",
                    240));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 09:30",
                    60));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00",
                    10));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 11:30",
                    30));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:10",
                    10));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.NEW, "20.08.24 10:30",
                    60));
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        try {
            taskManager.createTask(new Task("Task", "TaskType", Status.DONE));
            // size = 5
        } catch (TimeIntersectingException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(taskManager.getPrioritizedTasks().size(), 5);
    }

    @Test
    void deleteMethodsTest() {
        taskManager.createTask(task);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(epic.getId(), subtask);
        taskManager.createSubtask(epic.getId(), subtask);
        assertEquals(taskManager.getAllTasks().size(), 5);

        taskManager.deleteAllTask();
        assertEquals(taskManager.getAllTasks().size(), 3);

        taskManager.deleteAllSubtask();
        assertEquals(taskManager.getAllTasks().size(), 1);

        taskManager.createSubtask(epic.getId(), subtask);
        taskManager.createSubtask(epic.getId(), subtask);
        taskManager.deleteAllEpic();
        assertEquals(taskManager.getAllTasks().size(), 0);
    }
}
