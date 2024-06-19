package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getAllTasksAndEpic();

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

}
