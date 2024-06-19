package service;

import model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int historySize = 10;
    private List<Task> listTask = new ArrayList<>(historySize);

    @Override
    public void add(Task task) {
        if (listTask.size() == historySize) {
            listTask.removeFirst();
        }
        listTask.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return listTask;
    }
}
