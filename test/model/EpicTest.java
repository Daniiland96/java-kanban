package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    @Test
    void epicsWithSameIdAreEqual() {
        Epic epic1 = new Epic("Epic1", "EpicFirst");
        epic1.setId(1);
        Epic epic2 = new Epic("Epic2", "EpicSecond");
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}