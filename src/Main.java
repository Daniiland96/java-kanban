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

        backedTaskManager.createTask(task);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.id, subtask);

        System.out.println(backedTaskManager.getAllTasks());
        System.out.println();
        System.out.println(backedTaskManager.getPrioritizedTasks());
    }
}
