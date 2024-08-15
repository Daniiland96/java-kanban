package model;

public class Subtask extends Task {
    private int epicId;
    public final TypeTask typeTask = TypeTask.SUBTASK;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s\n", id, typeTask, title, status, description, getEpicId());
    }
}
