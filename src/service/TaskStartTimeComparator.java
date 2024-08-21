package service;

import model.Task;

import java.util.Comparator;

public class TaskStartTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        return o1.startTime.compareTo(o2.startTime);
    }
}
