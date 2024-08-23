package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    HistoryManager historyManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void createTaskManagerTest() {
        task = new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00", 60);
        epic = new Epic("Epic", "EpicType");
        subtask = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 11:00", 60);
    }

    @Test
    abstract void epicCannotBeAddItself();

    @Test
    abstract void canNotMakeSubtaskTheEpic();

    @Test
    abstract void taskManagerCreateAllTypeTaskAndReturnById();

    @Test
    abstract void createdIdDoNotConflictWithGenerated();

    @Test
    abstract void taskManagerDoesNotChangeFields();

    @Test
    abstract void epicRemoveSubtaskIdWhichWasDeleted();

    @Test
    abstract void getEpicsSubtasksTest();

    @Test
    abstract void subtaskHaveEpicId();
}