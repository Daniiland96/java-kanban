import java.util.Objects;

public class Task {
    public int id;
    public String title;
    public String description;
    public Status status;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = result + 31 * Objects.hashCode(description);
        result = result + 7 * id;
        return result;
    }

    @Override
    public String toString() {
        return getClass() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
