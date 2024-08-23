package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task {

    private ArrayList<Integer> arraySubtask = new ArrayList<>();
    public LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.typeTask = TypeTask.EPIC;
    }

    public ArrayList<Integer> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(ArrayList<Integer> arraySubtask) {
        this.arraySubtask = arraySubtask;
    }

    private String dateTimeToString() {
        String resultStart = "notSpecified";
        String resultEnd = "notSpecified";
        String resultDuration = "notSpecified";
        Optional<LocalDateTime> start = Optional.ofNullable(startTime);
        Optional<LocalDateTime> end = Optional.ofNullable(endTime);
        Optional<Duration> dur = Optional.ofNullable(duration);
        if (start.isPresent()) resultStart = start.get().format(DATE_TIME_FORMATTER);
        if (end.isPresent()) resultEnd = end.get().format(DATE_TIME_FORMATTER);
        if (dur.isPresent()) resultDuration = String.valueOf(dur.get().toMinutes());
        return String.format("%s,%s,%s", resultStart, resultEnd, resultDuration);
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
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, typeTask, title, status, description, subtaskList,
                dateTimeToString());
    }
}
