import model.*;
import service.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println();
        // Интерфейс пользователя

        TaskManager manager = Managers.getDefault();

        while (true) {
            printMenu();
            int cmd = scanner.nextInt();
            switch (cmd) {
                case 1:
                    System.out.println(manager.getAllTasksAndEpic());
                    break;
                case 2:
                    manager.deleteAllTasks();
                    break;
                case 3:
                    System.out.print("Введите id задачи: ");
                    int id = scanner.nextInt();
                    System.out.println(manager.getAnyTaskById(id));
                    break;
                case 4:
                    manager.createTask(createNewTask());
                    break;
                case 5:
                    manager.createEpic(createNewEpic());
                    break;
                case 6:
                    System.out.print("Введите id Epic, которому будет принадлежать Subtask: ");
                    int epicId = scanner.nextInt();
                    manager.createSubtask(epicId, createNewSubtask());
                    break;
                case 7:
                    System.out.print("Введите id Task, который хотите обновить: ");
                    int oldTaskId = scanner.nextInt();
                    manager.updateTask(oldTaskId, createNewTask());
                    break;
                case 8:
                    System.out.print("Введите id Epic, который хотите обновить: ");
                    int oldEpicId = scanner.nextInt();
                    manager.updateEpic(oldEpicId, createNewEpic());
                    break;
                case 9:
                    System.out.print("Введите id Subtask, который хотите обновить: ");
                    int oldSubtaskId = scanner.nextInt();
                    manager.updateSubtask(oldSubtaskId, createNewSubtask());
                    break;
                case 10:
                    System.out.print("Введите id задачи: ");
                    int deleteId = scanner.nextInt();
                    manager.deleteAnyTaskById(deleteId);
                    break;
                case 11:
                    System.out.print("Введите id Epic, подзадачи которого хотите посмотреть: ");
                    int epicIdShowSubtask = scanner.nextInt();
                    System.out.println(manager.getEpicsSubtasks(epicIdShowSubtask));
                    break;
                case 12:
                    System.out.println(manager.getHistory());
                    break;
                case 13:
                    return;
                default:
                    System.out.println("Введена неизвестная команда.");
                    System.out.println();
                    break;
            }
        }
    }

    static public void printMenu() {
        System.out.println();
        System.out.println("Введите одну из возможных команд:");
        System.out.println("1 - Получить список всех задач.");
        System.out.println("2 - Удалить все задачи.");
        System.out.println("3 - Получить задачу по ее идентификатору.");
        System.out.println("4 - Создать задачу типа Task.");
        System.out.println("5 - Создать задачу типа Epic.");
        System.out.println("6 - Создать задачу типа Subtask.");
        System.out.println("7 - Обновить задачу типа Task.");
        System.out.println("8 - Обновить задачу типа Epic.");
        System.out.println("9 - Обновить задачу типа Subtask.");
        System.out.println("10 - Удалить задачу по ее идентификатору.");
        System.out.println("11 - Получить для Epic список его подзадач Subtask.");
        System.out.println("12 - Получить историю просмотров.");
        System.out.println("13 - Выход.");
    }

    static public Task createNewTask() {
        scanner.nextLine();
        System.out.print("Введите имя: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        System.out.println("Введите статус задачи, где: ");
        Status status;
        while (true) {
            status = getStatus();
            if (status == null) {
                System.out.println("Стату введен неверно, попробуйте снова:");
            } else {
                break;
            }
        }
        return new Task(title.trim(), description.trim(), status);
    }

    static public Epic createNewEpic() {
        scanner.nextLine();
        System.out.print("Введите имя: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        return new Epic(title.trim(), description.trim());
    }

    static public Subtask createNewSubtask() {
        scanner.nextLine();
        System.out.print("Введите имя: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        System.out.println("Введите статус задачи, где: ");
        Status status;
        while (true) {
            status = getStatus();
            if (status == null) {
                System.out.println("Стату введен неверно, попробуйте снова:");
            } else {
                break;
            }
        }
        return new Subtask(title.trim(), description.trim(), status);
    }

    static public Status getStatus() {
        System.out.println("1 - NEW");
        System.out.println("2 - IN_PROGRESS");
        System.out.println("3 - DONE");
        Status status;
        int cmd = scanner.nextInt();
        switch (cmd) {
            case 1:
                status = Status.NEW;
                break;
            case 2:
                status = Status.IN_PROGRESS;
                break;
            case 3:
                status = Status.DONE;
                break;
            default:
                return null;
        }
        return status;
    }
}
