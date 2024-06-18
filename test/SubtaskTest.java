import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void subtaskWithSameIdAreEqual() {
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskFirst", Status.NEW);
        subtask1.id = 1;
        Subtask subtask2 = new Subtask("Subtask2", "SubtaskSecond", Status.DONE);
        subtask2.id = 1;
        assertEquals(subtask1, subtask2);
    }
}