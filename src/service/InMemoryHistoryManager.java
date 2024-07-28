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
        if (nodeStore.containsKey(task.id)) {
            Node node = nodeStore.get(task.id);
            node.task = task;
            removeNode(node);
            smartLinkedList.linkLast(node);
        } else {
            Node node = new Node(task);
            smartLinkedList.linkLast(node);
            nodeStore.put(task.id, node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return smartLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (nodeStore.containsKey(id)) {
            removeNode(nodeStore.get(id));
            nodeStore.remove(id);
        }
    }

    private void removeNode(Node node) {
        if (node.equals(smartLinkedList.head)) {
            if (node.equals(smartLinkedList.tail)) {
                smartLinkedList.head = null;
                smartLinkedList.tail = null;
            } else {
                Node nextNode = node.next;
                nextNode.previous = null;
                smartLinkedList.head = nextNode;
            }
        } else if (node.equals(smartLinkedList.tail)) {
            Node prevNode = node.previous;
            prevNode.next = null;
            smartLinkedList.tail = prevNode;
        } else {
            Node prevNode = node.previous;
            Node nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.previous = prevNode;
        }
    }

    static class SmartLinkedList {
        private Node head = null;
        private Node tail = null;

        private void linkLast(Node node) {
            if (head == null) {
                head = node;
                tail = node;
            } else {
                node.previous = tail;
                node.next = null;
                tail.next = node;
                tail = node;
            }
        }

        private List<Task> getTasks() {
            List<Task> listTask = new ArrayList<>();
            Node node = head;
            while (node != null) {
                listTask.add(node.task);
                node = node.next;
            }
            return listTask;
        }
    }
}
