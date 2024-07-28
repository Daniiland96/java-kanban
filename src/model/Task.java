package model;

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
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return getClass() + "{" +
                "id = " + id +
                ", title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", status = " + status +
                '}';
    }
}
