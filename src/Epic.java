import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> arraySubtask = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    @Override
    public String toString() {
        return getClass() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", arraySubtask=" + arraySubtask +
                '}';
    }
}
