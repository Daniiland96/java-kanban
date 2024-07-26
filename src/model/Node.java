package model;

public class Node {
    public Task task;
    public Node next = null;
    public Node previous = null;

    public Node(Task task) {
        this.task = task;
    }
}
