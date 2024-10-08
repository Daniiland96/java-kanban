package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeTest {
    @Test
    void NodesEqualIfTasksIdEqual() {
        Task task1 = new Task("Task1", "t1", Status.NEW,
                "20.08.24 10:00", 60);
        Task task2 = new Task("Task2", "t2", Status.DONE,
                "20.08.24 10:00", 60);
        task1.setId(1);
        task2.setId(1);

        Node node1 = new Node(task1);
        Node node2 = new Node(task2);

        Assertions.assertEquals(node1, node2);
    }
}
