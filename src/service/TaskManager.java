package service;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task getAnyTaskById(int id);

    void createTask(Task newTask);

    void createEpic(Epic newEpic);

    void createSubtask(int epicId, Subtask newSubtask);

    void updateTask(int taskId, Task newTask);

    void updateEpic(int epicId, Epic newEpic);

    void updateSubtask(int subtaskId, Subtask newSubtask);

    void deleteAnyTaskById(int id);

    ArrayList<Subtask> getEpicsSubtasks(int epicId);

    List<Task> getHistory();

    void removeTaskFromHistory(int id);

    Set<Task> getPrioritizedTasks();
}
