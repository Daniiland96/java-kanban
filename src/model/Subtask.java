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
        return getClass() + "{" +
                "epicId = " + epicId +
                ", id = " + id +
                ", title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", status = " + status +
                '}';
    }
}
