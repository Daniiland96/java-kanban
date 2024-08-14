package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            buffer.write("id,type,name,status,description,epic\n");
            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    buffer.write(taskToString(task));
                }
            }
            if (!epics.isEmpty()) {
                for (Epic task : epics.values()) {
                    buffer.write(epicToString(task));
                }
            }
            if (!subtasks.isEmpty()) {
                for (Subtask task : subtasks.values()) {
                    buffer.write(subtaskToString(task));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файле.");
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
        try (BufferedReader buffer = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            int allId = -1;
            String[] parts;
            String line = buffer.readLine();
            while (buffer.ready()) {
                line = buffer.readLine();
                parts = splitString(line);
                int id = Integer.parseInt(parts[0]);
                if (allId < id) allId = id;
                if (TypeTask.TASK.equals(TypeTask.valueOf(parts[1]))) {
                    Task task = taskFromStringArray(parts);
                    manager.tasks.put(id, task);
                }
                if (TypeTask.EPIC.equals(TypeTask.valueOf(parts[1]))) {
                    Epic epic = epicFromStringArray(parts);
                    manager.epics.put(id, epic);
                }
                if (TypeTask.SUBTASK.equals(TypeTask.valueOf(parts[1]))) {
                    Subtask subtask = subtaskFromStringArray(parts);
                    manager.subtasks.put(id, subtask);
                }
            }
            if (allId != -1) manager.allTaskId = allId;
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файле.");
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Передан неверный формат.");
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return manager;
    }

    private static String[] splitString(String line) {
        return line.split(",");
    }

    private static Task taskFromStringArray(String[] parts) {
        Task task = new Task(parts[2], parts[4], Status.valueOf(parts[3]));
        task.id = Integer.parseInt(parts[0]);
        return task;
    }

    private static Epic epicFromStringArray(String[] parts) {
        Epic epic = new Epic(parts[2], parts[4]);
        epic.id = Integer.parseInt(parts[0]);
        epic.status = Status.valueOf(parts[3]);
        if (parts[5].equals("empty")) return epic;
        String[] subtaskIdArray = parts[5].split("/");
        for (String id : subtaskIdArray) {
            epic.getArraySubtask().add(Integer.parseInt(id));
        }
        return epic;
    }

    private static Subtask subtaskFromStringArray(String[] parts) {
        Subtask subtask = new Subtask(parts[2], parts[4], Status.valueOf(parts[3]));
        subtask.id = Integer.parseInt(parts[0]);
        subtask.setEpicId(Integer.parseInt(parts[5]));
        return subtask;
    }

    private String taskToString(Task task) {
        return String.format("%s,%s,%s,%s,%s\n", task.id, task.typeTask, task.title, task.status,
                task.description);
    }

    private String epicToString(Epic task) {
        String subtaskList = "empty";
        if (!task.getArraySubtask().isEmpty()) {
            List<Integer> array = task.getArraySubtask();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (array.size() - 1); i++) {
                builder.append(array.get(i)).append("/");
            }
            builder.append(array.getLast());
            subtaskList = builder.toString();
        }
        return String.format("%s,%s,%s,%s,%s,%s\n", task.id, task.typeTask, task.title, task.status,
                task.description, subtaskList);
    }

    private String subtaskToString(Subtask task) {
        return String.format("%s,%s,%s,%s,%s,%s\n", task.id, task.typeTask, task.title, task.status,
                task.description, task.getEpicId());
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
    }

    @Override
    public void createSubtask(int epicId, Subtask newSubtask) {
        super.createSubtask(epicId, newSubtask);
        save();
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        super.updateTask(taskId, newTask);
        save();
    }

    @Override
    public void updateEpic(int epicId, Epic newEpic) {
        super.updateEpic(epicId, newEpic);
        save();
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        super.updateSubtask(subtaskId, newSubtask);
        save();
    }

    @Override
    public void deleteAnyTaskById(int id) {
        super.deleteAnyTaskById(id);
        save();
    }
}
