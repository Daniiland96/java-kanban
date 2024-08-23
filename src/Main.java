import model.*;

import service.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        File file = new File("manager.csv");

        TaskManager backedTaskManager = Managers.getDefault(file);

        Task task1 = new Task("Task", "description", Status.NEW, "10.05.24 10:00", 60);
        Task task2 = new Task("Task", "description", Status.NEW, "10.05.24 12:00", 60);
        Task task3 = new Task("Task", "description", Status.NEW, "10.05.24 14:00", 60);

        //Epic epic = new Epic("Epic", "description");

       // Subtask subtask = new Subtask("Subtask", "description", Status.IN_PROGRESS,
//                "10.05.24 12:00", 60);
        Subtask subtask2 = new Subtask("Subtask2", "description", Status.IN_PROGRESS,
                "10.05.24 09:30", 60);

        Task task = new Task("Task", "TaskType", Status.NEW, "20.08.24 10:00", 60);
        Epic epic = new Epic("Epic", "EpicType");
        Subtask subtask = new Subtask("Subtask", "SubtaskType", Status.NEW,
                "20.08.24 11:00", 60);

        backedTaskManager.createTask(task);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.id, subtask);

        /*backedTaskManager.createTask(task1);
        backedTaskManager.createTask(task2);
        backedTaskManager.createTask(task3);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.id, subtask);
        backedTaskManager.createSubtask(epic.id, subtask2);

        System.out.println(backedTaskManager.getPrioritizedTasks());
        System.out.println();
        backedTaskManager.updateTask(task2.id, new Task("Task", "DDDDDDDDD", Status.NEW,
                "10.05.24 09:00", 500));
        System.out.println(backedTaskManager.getPrioritizedTasks());
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.id, subtask);
        backedTaskManager.createSubtask(epic.id, subtask2);*/

    }
}
