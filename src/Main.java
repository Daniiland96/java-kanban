import model.*;

import service.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        // UserInterface.useInterface(manager); // готовый интерфейс пользователя

        Task task1 = new Task("Task1", "t1", Status.NEW);
        Task task2 = new Task("Task2", "t2", Status.IN_PROGRESS);
        Epic epic3 = new Epic("Epic3", "e3");
        Subtask subtask3Id4 = new Subtask("Subtask3_4", "s3_4", Status.IN_PROGRESS);
        Subtask subtask3Id5 = new Subtask("Subtask3_5", "s3_5", Status.IN_PROGRESS);
        Subtask subtask3Id6 = new Subtask("Subtask3_6", "s3_6", Status.IN_PROGRESS);
        Epic epic7 = new Epic("Epic7", "e7");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic3);
        manager.createSubtask(3, subtask3Id4);
        manager.createSubtask(3, subtask3Id5);
        manager.createSubtask(3, subtask3Id6);
        manager.createEpic(epic7);

        manager.getAnyTaskById(task1.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(task1.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(epic3.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(task2.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(epic3.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(subtask3Id4.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(epic7.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(subtask3Id4.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(subtask3Id5.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.getAnyTaskById(subtask3Id6.id);
        System.out.println(manager.getHistory());
        System.out.println();

        //remove

        manager.removeTaskFromHistory(task1.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.removeTaskFromHistory(epic7.id);
        System.out.println(manager.getHistory());
        System.out.println();

        //delete

        manager.deleteAnyTaskById(epic3.id);
        System.out.println(manager.getHistory());
        System.out.println();

        manager.deleteAnyTaskById(task2.id);
        System.out.println(manager.getHistory());
        System.out.println();
    }
}
