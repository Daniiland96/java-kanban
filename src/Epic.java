import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> arraySubtask = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(ArrayList<Integer> arraySubtask) {
        this.arraySubtask = arraySubtask;
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
