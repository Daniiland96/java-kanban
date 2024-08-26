package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

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
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getTitle(), getStatus(),
                getDescription(), getEpicId(), dateTimeToString());
    }
}