package model;

public class Node {
    public Task task;
    public Node next = null;
    public Node previous = null;

    public Node(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return task.equals(node.task);
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "task = " + task +
                ", nextTaskId = " + next.task.getId() +
                ", previousTaskId = " + previous.task.getId() +
                '}';
    }
}
