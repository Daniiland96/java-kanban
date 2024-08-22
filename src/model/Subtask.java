package model;

public class Subtask extends Task {
    private int epicId;
    public final TypeTask typeTask = TypeTask.SUBTASK;

    public Subtask(String title, String description, Status status, String startTime, int duration) {
        super(title, description, status, startTime, duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, typeTask, title, status, description, getEpicId(),
                startTime.format(DATE_TIME_FORMATTER), getEndTime().format(DATE_TIME_FORMATTER), duration.toMinutes());
    }
}