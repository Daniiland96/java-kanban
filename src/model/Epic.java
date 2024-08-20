package model;

import java.util.ArrayList;

public class Epic extends Task {
    public final TypeTask typeTask = TypeTask.EPIC;
    private ArrayList<Integer> arraySubtask = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(ArrayList<Integer> arraySubtask) {
        this.arraySubtask = arraySubtask;
    }

    @Override
    public String toString() {
        String subtaskList = "empty";
        if (!arraySubtask.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (arraySubtask.size() - 1); i++) {
                builder.append(arraySubtask.get(i)).append("/");
            }
            builder.append(arraySubtask.getLast());
            subtaskList = builder.toString();
        }
        return String.format("%s,%s,%s,%s,%s,%s", id, typeTask, title, status, description, subtaskList);
    }
}
