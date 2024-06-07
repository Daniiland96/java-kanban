import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int allTaskId = 1;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public String getTitleAllTasks() {
        String result;

        if (tasks.isEmpty()) {
            result = "Список Task пуст.\n";
        } else {
            result = "Список Task:\n";
            for (int taskId : tasks.keySet()) {
                result += taskId + " - " + tasks.get(taskId).title + "\n";
            }
        }

        if (epics.isEmpty()) {
            result += "Список Epic пуст.\n";
        } else {
            result += "Список Epic:\n";
            for (int epicId : epics.keySet()) {
                result += epicId + " - " + epics.get(epicId).title + "\n";
            }
        }

        return result;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        allTaskId = 1;
    }

    public Object getAnyTaskById(int id) {
        Object result = null;

        if (tasks.containsKey(id)) {
            result = tasks.get(id);

        } else if (epics.containsKey(id)) {
            result = epics.get(id);

        } else if (subtasks.containsKey(id)) {
            result = subtasks.get(id);

        } else {
            result = "Задача, с указанным id, не найдена.";
        }

        return result;
    }

    public void createTask(Task newTask) {
        if (tasks.containsValue(newTask)) {
            System.out.println("Такой Task уже существует.");

        } else {
            newTask.id = allTaskId;
            tasks.put(newTask.id, newTask);
            allTaskId++;
        }
    }

    public void createEpic(Epic newEpic) {
        if (epics.containsValue(newEpic)) {
            System.out.println("Такой Epic уже существует.");

        } else {
            newEpic.id = allTaskId;
            epics.put(newEpic.id, newEpic);
            updateEpicStatus(newEpic.id);
            allTaskId++;
        }
    }

    public void createSubtask(int epicId, Subtask newSubtask) {
        if (epics.isEmpty()) {
            System.out.println("Нельзя создать Subtask, пока нет ни одного Epic.");

        } else if (epics.containsKey(epicId)) {
            newSubtask.epicId = epicId;
            if (subtasks.containsValue(newSubtask)) {
                System.out.println("Такой Subtask уже существует в указанном Epic.");
            } else {
                newSubtask.id = allTaskId;
                subtasks.put(newSubtask.id, newSubtask);
                Epic epic = epics.get(epicId);
                epic.arraySubtask.add(newSubtask.id);
                updateEpicStatus(epicId);
                allTaskId++;
            }

        } else {
            System.out.println("Epic, с указанным id, не найден.");
        }
    }

    public void updateTask(int taskId, Task newTask) {
        if (tasks.containsKey(taskId)) {
            newTask.id = taskId;
            tasks.put(taskId, newTask);

        } else {
            System.out.println("Task, с указанным id, не найден.");
        }
    }

    public void updateEpic(int epicId, Epic newEpic) {
        if (epics.containsKey(epicId)) {
            newEpic.id = epicId;
            newEpic.arraySubtask = epics.get(epicId).arraySubtask;
            epics.put(epicId, newEpic);
            updateEpicStatus(epicId);

        } else {
            System.out.println("Epic, с указанным id, не найден.");
        }
    }

    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        if (epics.isEmpty()) {
            System.out.println("Список Epic и Subtask пуст.");

        } else if (subtasks.containsKey(subtaskId)) {
            newSubtask.id = subtaskId;
            newSubtask.epicId = subtasks.get(subtaskId).epicId;
            subtasks.put(subtaskId, newSubtask);
            updateEpicStatus(newSubtask.epicId);

        } else {
            System.out.println("Subtask, с указанным id, не найден.");
        }
    }

    public void deleteAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

        } else if (epics.containsKey(id)) {
            for (int epicsSubtaskId : epics.get(id).arraySubtask) {
                subtasks.remove(epicsSubtaskId);
            }
            epics.remove(id);

        } else if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).epicId;
            ArrayList<Integer> arraySubtaskByEpic = epics.get(epicId).arraySubtask;
            int count = 0;
            for (int subtaskId : arraySubtaskByEpic) {
                if (subtaskId == id) {
                    arraySubtaskByEpic.remove(count);
                    break;
                }
                count++;
            }
            subtasks.remove(id);
            updateEpicStatus(epicId);

        } else {
            System.out.println("Задача, с указанным id, не найдена.");
        }
    }

    public String getTitleEpicSubtasks(int epicId) {
        String result;

        if (epics.isEmpty()) {
            result = "Список Epic пуст.";

        } else if (epics.containsKey(epicId)) {
            ArrayList<Integer> arraySubtaskByEpic = epics.get(epicId).arraySubtask;
            if (arraySubtaskByEpic.isEmpty()) {
                result = "В указанном Epic еше нет подзадач.";
            } else {
                result = "Epic " + epicId + " - " + epics.get(epicId).title + "\n";
                for (int subtaskId : arraySubtaskByEpic) {
                    result += "Subtask " + subtaskId + " - " + subtasks.get(subtaskId).title + "\n";
                }
            }

        } else {
            result = "Epic, с указанным id, не найден.";
        }

        return result;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic.arraySubtask.isEmpty()) {
            epic.status = Status.NEW;

        } else {
            int countNew = 0;
            int countDone = 0;
            for (int subtaskId : epic.arraySubtask) {
                Status status = subtasks.get(subtaskId).status;
                switch (status) {
                    case NEW:
                        countNew++;
                        break;
                    case DONE:
                        countDone++;
                        break;
                    default:
                        break;
                }
            }
            if (countNew == epic.arraySubtask.size()) {
                epic.status = Status.NEW;
            } else if (countDone == epic.arraySubtask.size()) {
                epic.status = Status.DONE;
            } else {
                epic.status = Status.IN_PROGRESS;
            }
        }
    }

}

