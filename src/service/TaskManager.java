package service;

import model.*;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    void deleteAllTasks();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    Task getAnyTaskById(int id);

    void createTask(Task newTask);

    void createEpic(Epic newEpic);

    void createSubtask(int epicId, Subtask newSubtask);

    void updateTask(int taskId, Task newTask);

    void updateEpic(int epicId, Epic newEpic);

    void updateSubtask(int subtaskId, Subtask newSubtask);

    void deleteAnyTaskById(int id);

    List<Subtask> getEpicsSubtasks(int epicId);

    List<Task> getHistory();

    void removeTaskFromHistory(int id);

    List<Task> getPrioritizedTasks();
}
