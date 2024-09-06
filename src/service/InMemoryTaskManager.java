package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int allTaskId = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager;
    protected TaskStartTimeComparator startTimeComparator = new TaskStartTimeComparator();
    protected Set<Task> prioritizedTasks = new TreeSet<>(startTimeComparator);

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<>();
        result.addAll(tasks.values());
        result.addAll(epics.values());
        result.addAll(subtasks.values());
        return result;
    }

    @Override
    public List<Task> getAllTask() {
        return List.copyOf(tasks.values());
    }

    @Override
    public List<Task> getAllEpic() {
        return List.copyOf(epics.values());
    }

    @Override
    public List<Task> getAllSubtask() {
        return List.copyOf(subtasks.values());
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
    public void deleteAllTask() {
        tasks.values().forEach(task -> {
            prioritizedTasks.remove(task);
            removeTaskFromHistory(task.getId());
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        List<Integer> copy = List.copyOf(epics.keySet());
        copy.forEach(this::deleteAnyTaskById);
    }

    @Override
    public void deleteAllSubtask() {
        List<Integer> copy = List.copyOf(subtasks.keySet());
        copy.forEach(this::deleteAnyTaskById);
    }

    @Override
    public Task getAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            throw new NotFoundException("Задача с id: " + id + " не найдена.");
        }
    }

    @Override
    public void createTask(Task newTask) {
        if (isTaskCanAddToList(newTask, prioritizedTasks)) {
            newTask.setId(generateAllTaskId());
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
        } else {
            throw new TimeIntersectingException("Время выполнения задачи пересекается с другими задачами.");
        }
    }

    @Override
    public void createEpic(Epic newEpic) {
        newEpic.setId(generateAllTaskId());
        epics.put(newEpic.getId(), newEpic);
        updateEpicStatus(newEpic.getId());
        updateEpicDateTime(newEpic.getId());
    }

    @Override
    public void createSubtask(int epicId, Subtask newSubtask) {
        if (epics.isEmpty()) {
            throw new NotFoundException("Нельзя создать Subtask, пока нет ни одного Epic.");
        } else if (epics.containsKey(epicId)) {
            if (isTaskCanAddToList(newSubtask, prioritizedTasks)) {
                newSubtask.setEpicId(epicId);
                newSubtask.setId(generateAllTaskId());
                subtasks.put(newSubtask.getId(), newSubtask);
                prioritizedTasks.add(newSubtask);
                Epic epic = epics.get(epicId);
                epic.getArraySubtask().add(newSubtask.getId());
                updateEpicStatus(epicId);
                updateEpicDateTime(epicId);
            } else {
                throw new TimeIntersectingException("Время выполнения задачи пересекается с другими задачами.");
            }
        } else {
            throw new NotFoundException("Epic, с id: " + epicId + " не найден.");
        }
    }

    @Override
    public void updateTask(int taskId, Task newTask) {
        if (tasks.containsKey(taskId)) {
            newTask.setId(taskId);
            if (isTaskCanAddToList(newTask, prioritizedTasks)) {
                prioritizedTasks.remove(tasks.get(taskId));
                prioritizedTasks.add(newTask);
                tasks.put(taskId, newTask);
            } else {
                throw new TimeIntersectingException("Время выполнения задачи пересекается с другими задачами.");
            }
        } else {
            throw new NotFoundException("Task, с id: " + taskId + " не найден.");
        }
    }

    @Override
    public void updateEpic(int epicId, Epic newEpic) {
        if (epics.containsKey(epicId)) {
            newEpic.setId(epicId);
            List<Integer> oldArray = epics.get(epicId).getArraySubtask();
            newEpic.setArraySubtask(oldArray);
            epics.put(epicId, newEpic);
            updateEpicStatus(epicId);
            updateEpicDateTime(epicId);
        } else {
            throw new NotFoundException("Epic, с id: " + epicId + " не найден.");
        }
    }

    @Override
    public void updateSubtask(int subtaskId, Subtask newSubtask) {
        if (epics.isEmpty()) {
            throw new NotFoundException("Список Epic и Subtask пуст.");
        } else if (subtasks.containsKey(subtaskId)) {
            newSubtask.setId(subtaskId);
            if (isTaskCanAddToList(newSubtask, prioritizedTasks)) {
                int oldEpicId = subtasks.get(subtaskId).getEpicId();
                newSubtask.setEpicId(oldEpicId);
                prioritizedTasks.remove(subtasks.get(subtaskId));
                prioritizedTasks.add(newSubtask);
                subtasks.put(subtaskId, newSubtask);
                updateEpicStatus(newSubtask.getEpicId());
                updateEpicDateTime(newSubtask.getEpicId());
            } else {
                throw new TimeIntersectingException("Время выполнения задачи пересекается с другими задачами.");
            }
        } else {
            throw new NotFoundException("Subtask, с id: " + subtaskId + " не найден.");
        }
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
            List<Integer> arraySubtaskByEpic = epics.get(epicId).getArraySubtask();
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

        } else {
            throw new NotFoundException("Задача, с id: " + id + " не найден.");
        }
    }

    @Override
    public List<Subtask> getEpicsSubtasks(int epicId) {
        List<Subtask> result = new ArrayList<>();

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
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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
            epic.setStatus(Status.NEW);

        } else {
            int countNew = 0;
            int countDone = 0;
            for (int subtaskId : epic.getArraySubtask()) {
                Status status = subtasks.get(subtaskId).getStatus();
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
                epic.setStatus(Status.NEW);
            } else if (countDone == epic.getArraySubtask().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    protected void updateEpicDateTime(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> sortSubtask = new ArrayList<>();
        if (!epic.getArraySubtask().isEmpty()) {
            sortSubtask = epic.getArraySubtask().stream()
                    .map(id -> subtasks.get(id))
                    .filter(subtask -> !(subtask.getStartTime() == null || subtask.getDuration() == null))
                    .sorted(startTimeComparator)
                    .toList();
        }
        if (!sortSubtask.isEmpty()) {
            epic.setStartTime(sortSubtask.getFirst().getStartTime());
            epic.setEndTime(sortSubtask.getLast().getEndTime());
            long minutes = 0;
            for (Subtask subtask : sortSubtask) {
                minutes += subtask.getDuration().toMinutes();
            }
            epic.setDuration(Duration.ofMinutes(minutes));
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    private Boolean isDateTimeNotIntersects(Task testTask, Task otherTask) {
        if (testTask.getStartTime() == null || otherTask.getStartTime() == null) return true;
        if (testTask.getEndTime().isBefore(otherTask.getStartTime())
                || testTask.getEndTime().equals(otherTask.getStartTime())) return true;
        return testTask.getStartTime().isAfter(otherTask.getEndTime())
                || testTask.getStartTime().equals(otherTask.getEndTime());
    }

    private Boolean isTaskCanAddToList(Task newTask, Set<Task> prioritizedTasks) {
        if (prioritizedTasks.isEmpty()) return true;
        return !prioritizedTasks.stream()
                .filter(task -> !newTask.equals(task))
                .map(task -> isDateTimeNotIntersects(newTask, task))
                .anyMatch(booleanValue -> booleanValue == false);
    }
}


