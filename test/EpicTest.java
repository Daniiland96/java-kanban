import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void epicsWithSameIdAreEqual() {
        Epic epic1 = new Epic("Epic1", "EpicFirst");
        epic1.id = 1;
        Epic epic2 = new Epic("Epic2", "EpicSecond");
        epic2.id = 1;
        assertEquals(epic1, epic2);
    }
}