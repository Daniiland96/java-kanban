import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> listTask = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (listTask.size() == 10) {
            listTask.removeFirst();
            listTask.add(task);
        } else {
            listTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return listTask;
    }
}
