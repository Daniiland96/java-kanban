package server;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.TaskManager;

public class BaseServerTest {
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected TaskManager manager;

    @BeforeEach
    void startServerTest() {
        task1 = new Task("T1", "D1", Status.NEW);
        task2 = new Task("T2", "D2", Status.DONE, "20.08.24 10:00", 60);
        epic1 = new Epic("E1", "D1");
        epic2 = new Epic("E2", "D2");
        subtask1 = new Subtask("S1", "D1", Status.IN_PROGRESS);
        subtask2 = new Subtask("S2", "D2", Status.NEW,
                "20.08.24 11:00", 60);

        HttpTaskServer.startServer();
        manager = HttpTaskServer.manager;
    }

    @AfterEach
    void stopServerTest() {
        HttpTaskServer.stopServer();
    }
}
