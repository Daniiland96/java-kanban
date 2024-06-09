import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int allTaskId = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getAllTasksAndEpic() {
        ArrayList<Task> result = new ArrayList<>();

        result.addAll(tasks.values());
        result.addAll(epics.values());

        return result;
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        resetAllTaskId();
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
            newTask.id = generateAllTaskId();
            tasks.put(newTask.id, newTask);
        }
    }

    public void createEpic(Epic newEpic) {
        if (epics.containsValue(newEpic)) {
            System.out.println("Такой Epic уже существует.");

        } else {
            newEpic.id = generateAllTaskId();
            epics.put(newEpic.id, newEpic);
            updateEpicStatus(newEpic.id);
        }
    }

    public void createSubtask(int epicId, Subtask newSubtask) {
        if (epics.isEmpty()) {
            System.out.println("Нельзя создать Subtask, пока нет ни одного Epic.");

        } else if (epics.containsKey(epicId)) {
            newSubtask.setEpicId(epicId);
            if (subtasks.containsValue(newSubtask)) {
                System.out.println("Такой Subtask уже существует в указанном Epic.");
            } else {
                newSubtask.id = generateAllTaskId();
                subtasks.put(newSubtask.id, newSubtask);
                Epic epic = epics.get(epicId);
                epic.getArraySubtask().add(newSubtask.id);
                updateEpicStatus(epicId);
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
            ArrayList<Integer> oldArray = epics.get(epicId).getArraySubtask();
            newEpic.setArraySubtask(oldArray);
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
            int oldEpicId = subtasks.get(subtaskId).getEpicId();
            newSubtask.setEpicId(oldEpicId);
            subtasks.put(subtaskId, newSubtask);
            updateEpicStatus(newSubtask.getEpicId());

        } else {
            System.out.println("Subtask, с указанным id, не найден.");
        }
    }

    public void deleteAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

        } else if (epics.containsKey(id)) {
            for (int epicsSubtaskId : epics.get(id).getArraySubtask()) {
                subtasks.remove(epicsSubtaskId);
            }
            epics.remove(id);

        } else if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            ArrayList<Integer> arraySubtaskByEpic = epics.get(epicId).getArraySubtask();
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

    public ArrayList<Subtask> getEpicsSubtasks(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();

        if (epics.containsKey(epicId)) {
            ArrayList<Integer> arraySubtaskByEpic = epics.get(epicId).getArraySubtask();
            for (int subtaskId : arraySubtaskByEpic) {
                result.add(subtasks.get(subtaskId));
            }
        }

        return result;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic.getArraySubtask().isEmpty()) {
            epic.status = Status.NEW;

        } else {
            int countNew = 0;
            int countDone = 0;
            for (int subtaskId : epic.getArraySubtask()) {
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
            if (countNew == epic.getArraySubtask().size()) {
                epic.status = Status.NEW;
            } else if (countDone == epic.getArraySubtask().size()) {
                epic.status = Status.DONE;
            } else {
                epic.status = Status.IN_PROGRESS;
            }
        }
    }

    private int generateAllTaskId() {
        return ++allTaskId;
    }

    private void resetAllTaskId() {
        allTaskId = 0;
    }
}

