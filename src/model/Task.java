package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    public int id = -1;
    public String title;
    public String description;
    public Status status;
    public final TypeTask typeTask = TypeTask.TASK;
    public LocalDateTime startTime;
    public Duration duration;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public Task(String title, String description, Status status, String startTime, int duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, typeTask, title, status, description,
                startTime.format(DATE_TIME_FORMATTER), getEndTime().format(DATE_TIME_FORMATTER), duration.toMinutes());
    }
}
