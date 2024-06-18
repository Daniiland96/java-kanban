import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void tasksWithSameIdAreEqual() {
        Task task1 = new Task("Task1", "TaskFirst", Status.NEW);
        task1.id = 1;
        Task task2 = new Task("Task2", "TaskSecond", Status.DONE);
        task2.id = 1;
        assertEquals(task1, task2);
    }
}