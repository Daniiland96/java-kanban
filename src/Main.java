import model.*;

import service.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        File file = new File("manager.csv");

        TaskManager backedTaskManager = Managers.getDefault(file);

        Task task = new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00", 60);
        Epic epic = new Epic("Epic", "EpicType");
        Subtask subtask = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 11:00", 60);
        Task task1 = new Task("AAA", "AAA", Status.IN_PROGRESS);
        Task task2 = new Task("BBB", "BBB", Status.DONE);
        Subtask subtask1 = new Subtask("CCC", "CCC", Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 13:00", 60);

        backedTaskManager.createTask(task1);
        backedTaskManager.createTask(task2);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.getId(), subtask2);
        backedTaskManager.createSubtask(epic.getId(), subtask1);
        backedTaskManager.createSubtask(epic.getId(), subtask);
        backedTaskManager.createTask(task);

        System.out.println(backedTaskManager.getAllTasks());
        System.out.println();
        System.out.println(backedTaskManager.getPrioritizedTasks());
    }
}
