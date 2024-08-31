
package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskTest {
    private int id = -1;
    private String title;
    private String description;
    private Status status;
        private LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 20, 20);
    private LocalDateTime endTime = null;
        private Duration duration = Duration.ofMinutes(37);
     public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public TaskTest(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

//        public TaskTest(String title, String description, Status status) {
//            this.title = title;
//            this.description = description;
//            this.status = status;
//        }

      /*  public TaskTest(String title, String description, Status status, String startTime, int duration) {
            this.title = title;
            this.description = description;
            this.status = status;
            this.startTime = LocalDateTime.parse(startTime);
            this.duration = Duration.ofMinutes(duration);
        }

        public TaskTest(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Status getStatus() {
            return status;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public LocalDateTime getEndTime() {
            if (startTime == null || duration == null) return null;
            return startTime.plus(duration);
        }

        public TypeTask getType() {
            return TypeTask.TASK;
        }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskTest task = (TaskTest) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

       /* protected String dateTimeToString() {
            if (startTime == null || duration == null) {
                return String.format("%s,%s,%s", null, getEndTime(), null);
            }
            return String.format("%s,%s,%s", startTime,
                    getEndTime(), duration.toMinutes());
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s,%s", id, getType(), title, status, description, dateTimeToString());
        }*/
}
