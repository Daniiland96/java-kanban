package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    @Test
    void subtaskWithSameIdAreEqual() {
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskFirst", Status.NEW,
                "20.08.24 10:00", 60);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask2", "SubtaskSecond", Status.DONE,
                "20.08.24 11:00", 60);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }
}