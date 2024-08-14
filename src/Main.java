import model.*;

import service.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        //UserInterface.useInterface(manager); // готовый интерфейс пользователя
        //TaskManager manager = Managers.getDefault();
        File file = new File("manager.csv");
        TaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Task task = new Task("Task", "description", Status.NEW);
        Epic epic = new Epic("Epic", "description");
        Subtask subtask = new Subtask("Subtask", "description", Status.IN_PROGRESS);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(epic.id, subtask);

        TaskManager manager2 = FileBackedTaskManager.loadFromFile(file);

        System.out.println(manager.getAnyTaskById(task.id).equals(manager2.getAnyTaskById(task.id)));
        System.out.println(manager.getAnyTaskById(epic.id).equals(manager2.getAnyTaskById(epic.id)));
        System.out.println(manager.getAnyTaskById(subtask.id).equals(manager2.getAnyTaskById(subtask.id)));
    }
}
