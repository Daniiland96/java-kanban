package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> arraySubtask = new ArrayList<>();
    private LocalDateTime endTime = null;

    public Epic(String title, String description) {
        super(title, description);
    }

    public List<Integer> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(List<Integer> arraySubtask) {
        this.arraySubtask = arraySubtask;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    protected String dateTimeToString() {
        if (getStartTime() == null || getDuration() == null || endTime == null) {
            return String.format("%s,%s,%s", null, null, null);
        }
        return String.format("%s,%s,%s", getStartTime().format(DATE_TIME_FORMATTER),
                getEndTime().format(DATE_TIME_FORMATTER), getDuration().toMinutes());
    }

    @Override
    public String toString() {
        String subtaskList = null;
        if (!arraySubtask.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (arraySubtask.size() - 1); i++) {
                builder.append(arraySubtask.get(i)).append("/");
            }
            builder.append(arraySubtask.getLast());
            subtaskList = builder.toString();
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getTitle(), getStatus(), getDescription(),
                subtaskList, dateTimeToString());
    }
}
