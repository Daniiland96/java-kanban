import model.*;

import service.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        File file = new File("manager.csv");

//        TaskManager backedTaskManager = Managers.getDefault(file);

//        Task task = new Task("Task", "description", Status.NEW);
//        Epic epic = new Epic("Epic", "description");
//        Subtask subtask = new Subtask("Subtask", "description", Status.IN_PROGRESS);
//        Subtask subtask2 = new Subtask("Subtask2", "description", Status.IN_PROGRESS);

       /* backedTaskManager.createTask(task);
        backedTaskManager.createEpic(epic);
        backedTaskManager.createSubtask(epic.id, subtask);
        backedTaskManager.createSubtask(epic.id, subtask2);

        TaskManager manager2 = Managers.getDefault(file);

        System.out.println(backedTaskManager.getAnyTaskById(task.id).equals(manager2.getAnyTaskById(task.id)));
        System.out.println(backedTaskManager.getAnyTaskById(epic.id).equals(manager2.getAnyTaskById(epic.id)));
        System.out.println(backedTaskManager.getAnyTaskById(subtask.id).equals(manager2.getAnyTaskById(subtask.id)));
        System.out.println(backedTaskManager.getAnyTaskById(subtask2.id).equals(manager2.getAnyTaskById(subtask2.id)));*/

        LocalDateTime time = LocalDateTime.now();
        int x = -5;
        Duration test = Duration.ofMinutes(x);
        System.out.println(test);
    }
}
