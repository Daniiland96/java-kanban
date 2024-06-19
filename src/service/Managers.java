package service;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    private static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
