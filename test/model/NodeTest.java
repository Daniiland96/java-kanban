package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeTest {
    @Test
    void NodesEqualIfTasksIdEqual(){
        Task task1 = new Task("Task1", "t1", Status.NEW);
        Task task2 = new Task("Task2", "t2", Status.DONE);
        task1.id = 1;
        task2.id = 1;

        Node node1 = new Node(task1);
        Node node2 = new Node(task2);

        Assertions.assertEquals(node1, node2);
    }
}
