package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> nodeStore = new HashMap<>();
    SmartLinkedList smartLinkedList = new SmartLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) return;
        Node node;
        if (nodeStore.containsKey(task.getId())) {
            node = nodeStore.get(task.getId());
            node.task = task;
            removeNode(node);
        } else {
            node = new Node(task);
            nodeStore.put(task.getId(), node);
        }
        smartLinkedList.linkLast(node);
    }

    @Override
    public List<Task> getHistory() {
        return smartLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (!nodeStore.containsKey(id)) return;
        removeNode(nodeStore.get(id));
        nodeStore.remove(id);
    }

    private void removeNode(Node node) {
        Node prevNode = node.previous;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.previous = prevNode;
    }

    static class SmartLinkedList {
        private Node head;
        private Node tail;

        private SmartLinkedList() {
            head = new Node(null);
            tail = new Node(null);
            head.next = tail;
            tail.previous = head;
        }

        private void linkLast(Node node) {
            Node prevTail = tail.previous;
            prevTail.next = node;
            node.previous = prevTail;
            node.next = tail;
            tail.previous = node;
        }

        private List<Task> getTasks() {
            List<Task> listTask = new ArrayList<>();
            Node node = head.next;
            while (node != tail) {
                listTask.add(node.task);
                node = node.next;
            }
            return listTask;
        }
    }
}
