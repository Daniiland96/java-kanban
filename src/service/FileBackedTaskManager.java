package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    public static final String TITLE = "id,type,name,status,description,epic,startTime,endTime,duration\n";

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            buffer.write(TITLE);
            for (Task task : getAllTasks()) {
                buffer.write(task.toString() + "\n");
            }

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
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
                    manager.prioritizedTasks.add(task);
                }
                if (TypeTask.EPIC.equals(TypeTask.valueOf(parts[1]))) {
                    Epic epic = epicFromStringArray(parts);
                    manager.epics.put(id, epic);
                }
                if (TypeTask.SUBTASK.equals(TypeTask.valueOf(parts[1]))) {
                    Subtask subtask = subtaskFromStringArray(parts);
                    manager.subtasks.put(id, subtask);
                    manager.prioritizedTasks.add(subtask);
                }
            }
            if (allId != -1) manager.allTaskId = allId;
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Передан неверный формат.");
        }
        return manager;
    }

    private static String[] splitString(String line) {
        return line.split(",");
    }

    private static Task taskFromStringArray(String[] parts) {
        Task task;
        if (parts[5].equals("null") || parts[7].equals("null")) {
            task = new Task(parts[2], parts[4], Status.valueOf(parts[3]));
        } else {
            task = new Task(parts[2], parts[4], Status.valueOf(parts[3]), parts[5], Integer.parseInt(parts[7]));
        }
        task.setId(Integer.parseInt(parts[0]));
        return task;
    }

    private static Epic epicFromStringArray(String[] parts) {
        Epic epic = new Epic(parts[2], parts[4]);
        epic.setId(Integer.parseInt(parts[0]));
        epic.setStatus(Status.valueOf(parts[3]));

        if (parts[6].equals("null")) epic.setStartTime(null);
        else epic.setStartTime(LocalDateTime.parse(parts[6], Task.DATE_TIME_FORMATTER));

        if (parts[7].equals("null")) epic.setEndTime(null);
        else epic.setEndTime(LocalDateTime.parse(parts[7], Task.DATE_TIME_FORMATTER));

        if (parts[8].equals("null")) epic.setDuration(null);
        else epic.setDuration(Duration.ofMinutes(Integer.parseInt(parts[8])));

        if (parts[5].equals("null")) return epic;
        String[] subtaskIdArray = parts[5].split("/");
        for (String id : subtaskIdArray) {
            epic.getArraySubtask().add(Integer.parseInt(id));
        }
        return epic;
    }

    private static Subtask subtaskFromStringArray(String[] parts) {
        Subtask subtask;
        if (parts[6].equals("null") || parts[8].equals("null")) {
            subtask = new Subtask(parts[2], parts[4], Status.valueOf(parts[3]));
        } else {
            subtask = new Subtask(parts[2], parts[4], Status.valueOf(parts[3]), parts[6],
                    Integer.parseInt(parts[8]));
        }
        subtask.setId(Integer.parseInt(parts[0]));
        subtask.setEpicId(Integer.parseInt(parts[5]));
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
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
