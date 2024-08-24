package service;

import model.*;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int allTaskId = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HistoryManager historyManager;
    TaskStartTimeComparator startTimeComparator = new TaskStartTimeComparator();
    private Set<Task> prioritizedTasks = new TreeSet<>(startTimeComparator);

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();

        result.addAll(tasks.values());
        result.addAll(epics.values());
        result.addAll(subtasks.values());

        return result;
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(this::removeTaskFromHistory);
        tasks.clear();
        epics.keySet().forEach(this::removeTaskFromHistory);
        epics.clear();
        subtasks.keySet().forEach(this::removeTaskFromHistory);
        subtasks.clear();
        prioritizedTasks.clear();
        resetAllTaskId();
    }

    @Override
    public Task getAnyTaskById(int id) {
        Task result;

        if (tasks.containsKey(id)) result = tasks.get(id);
        else if (epics.containsKey(id)) result = epics.get(id);
        else if (subtasks.containsKey(id)) result = subtasks.get(id);
        else result = null;

        if (result != null) historyManager.add(result);

        return result;
    }

    @Override
    public void createTask(Task newTask) {
        if (isTaskCanAddToList(newTask, prioritizedTasks)) {
            newTask.id = generateAllTaskId();
            tasks.put(newTask.id, newTask);
            prioritizedTasks.add(newTask);
        } else System.out.println("Время выполнения задачи пересекается с другими задачами.");
    }

    @Override
    public void createEpic(Epic newEpic) {
        newEpic.id = generateAllTaskId();
        epics.put(newEpic.id, newEpic);
        updateEpicStatus(newEpic.id);
        updateEpicDateTime(newEpic.id);
    }

    @Override
    public void createSubtask(int epicId, Subtask newSubtask) {
        if (epics.isEmpty()) System.out.println("Нельзя создать Subtask, пока нет ни одного Epic.");
        else if (epics.containsKey(epicId)) {
            if (isTaskCanAddToList(newSubtask, prioritizedTasks)) {
                newSubtask.setEpicId(epicId);
                newSubtask.id = generateAllTaskId();
                subtasks.put(newSubtask.id, newSubtask);
                prioritizedTasks.add(newSubtask);
                Epic epic = epics.get(epicId);
                epic.getArraySubtask().add(newSubtask.id);
                updateEpicStatus(epicId);
                updateEpicDateTime(epicId);
            } else System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else System.out.println("Epic, с указанным id, не найден.");
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        if (tasks.containsKey(taskId)) {
            newTask.id = taskId;
            if (isTaskCanAddToList(newTask, prioritizedTasks)) {
                prioritizedTasks.remove(tasks.get(taskId));
                prioritizedTasks.add(newTask);
                tasks.put(taskId, newTask);
            } else System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else System.out.println("Task, с указанным id, не найден.");
    }

    @Override
    public void updateEpic(int epicId, Epic newEpic) {
        if (epics.containsKey(epicId)) {
            newEpic.id = epicId;
            ArrayList<Integer> oldArray = epics.get(epicId).getArraySubtask();
            newEpic.setArraySubtask(oldArray);
            epics.put(epicId, newEpic);
            updateEpicStatus(epicId);
            updateEpicDateTime(epicId);

        } else System.out.println("Epic, с указанным id, не найден.");
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        if (epics.isEmpty()) System.out.println("Список Epic и Subtask пуст.");
        else if (subtasks.containsKey(subtaskId)) {
            newSubtask.id = subtaskId;
            if (isTaskCanAddToList(newSubtask, prioritizedTasks)) {
                int oldEpicId = subtasks.get(subtaskId).getEpicId();
                newSubtask.setEpicId(oldEpicId);
                prioritizedTasks.remove(subtasks.get(subtaskId));
                prioritizedTasks.add(newSubtask);
                subtasks.put(subtaskId, newSubtask);
                updateEpicStatus(newSubtask.getEpicId());
                updateEpicDateTime(newSubtask.getEpicId());
            } else System.out.println("Время выполнения задачи пересекается с другими задачами.");
        } else System.out.println("Subtask, с указанным id, не найден.");
    }

    @Override
    public void deleteAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            removeTaskFromHistory(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);

        } else if (epics.containsKey(id)) {
            epics.get(id).getArraySubtask().forEach(epicsSubtaskId -> {
                removeTaskFromHistory(epicsSubtaskId);
                prioritizedTasks.remove(subtasks.get(epicsSubtaskId));
                subtasks.remove(epicsSubtaskId);
            });

            removeTaskFromHistory(id);
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
            removeTaskFromHistory(id);
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            updateEpicStatus(epicId);
            updateEpicDateTime(epicId);

        } else System.out.println("Задача, с указанным id, не найдена.");
    }

    @Override
    public ArrayList<Subtask> getEpicsSubtasks(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();

        if (epics.containsKey(epicId)) {
            List<Integer> arraySubtaskByEpic = epics.get(epicId).getArraySubtask();
            arraySubtaskByEpic.forEach(subtaskId -> result.add(subtasks.get(subtaskId)));
        }
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeTaskFromHistory(int id) {
        historyManager.remove(id);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private int generateAllTaskId() {
        return ++allTaskId;
    }

    private void resetAllTaskId() {
        allTaskId = 0;
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

    protected void updateEpicDateTime(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.getArraySubtask().isEmpty()) {
            List<Subtask> sortSubtask = epic.getArraySubtask().stream()
                    .map(id -> subtasks.get(id))
                    .sorted(startTimeComparator)
                    .toList();
            epic.startTime = sortSubtask.getFirst().startTime;
            epic.endTime = sortSubtask.getLast().getEndTime();
            long minutes = 0;
            for (Subtask subtask : sortSubtask) {
                minutes += subtask.duration.toMinutes();
            }
            epic.duration = Duration.ofMinutes(minutes);
        } else {
            epic.startTime = null;
            epic.endTime = null;
            epic.duration = null;
        }
    }

    private Boolean isDateTimeNotIntersects(Task testTask, Task otherTask) {
        if ((testTask.startTime.isAfter(otherTask.startTime) && testTask.startTime.isBefore(otherTask.getEndTime()))
                || testTask.startTime.equals(otherTask.startTime)) return false;
        else if ((testTask.getEndTime().isAfter(otherTask.startTime)
                && testTask.getEndTime().isBefore(otherTask.getEndTime()))
                || testTask.getEndTime().equals(otherTask.getEndTime())) return false;
        else if (testTask.startTime.isBefore(otherTask.startTime)
                && testTask.getEndTime().isAfter(otherTask.getEndTime())) return false;
        else return true;
    }

    private Boolean isTaskCanAddToList(Task newTask, Set<Task> prioritizedTasks) {
        if (prioritizedTasks.isEmpty()) return true;
        return !prioritizedTasks.stream()
                .filter(task -> !newTask.equals(task))
                .map(task -> isDateTimeNotIntersects(newTask, task))
                .anyMatch(booleanValue -> booleanValue == false);
    }
}


