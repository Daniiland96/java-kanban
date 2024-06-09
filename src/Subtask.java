import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subtask subtask = (Subtask) o;
        return Objects.equals(title, subtask.title) && (epicId == subtask.epicId);
    }

    @Override
    public int hashCode() {
        int result = 31 + 7 * Objects.hashCode(title);
        result = result + 31 * epicId;
        return result;
    }

    @Override
    public String toString() {
        return getClass() + "{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
