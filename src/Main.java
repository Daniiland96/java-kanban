import model.*;

import service.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println();
        // Интерфейс пользователя

        TaskManager manager = Managers.getDefault();
       // UserInterface.useInterface(manager);

        Task task1 = new Task("Task1", "t1", Status.NEW);
        Task task2 = new Task("Task2", "t2", Status.IN_PROGRESS);
        Task task3 = new Task("Task3", "t3", Status.DONE);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getAnyTaskById(task1.id);
        manager.getAnyTaskById(task2.id);
        manager.getAnyTaskById(task3.id);
        manager.getAnyTaskById(task1.id);

        System.out.println(manager.getHistory());
    }
}
