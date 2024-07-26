package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    //private final int historySize = 10;
    //private List<Task> listTask = new ArrayList<>();
    private Map<Integer, Node> nodeStore = new HashMap<>();
    SmartLinkedList smartLinkedList = new SmartLinkedList();

    @Override
    public void add(Task task) {
        if (nodeStore.containsKey(task.id)) {
            Node node = nodeStore.get(task.id);
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
        Node prevNode = node.previous;
        Node nextNode = node.next;
        if (prevNode != null) prevNode.next = nextNode;
        if (nextNode != null) nextNode.previous = prevNode;
        // не получится принудительно удалить единственный элемент ???
    }

    class SmartLinkedList {
        private Node head = null;
        private Node tail = null;

        private void linkLast(Node newNode) {
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.previous = tail;
                tail.next = newNode;
                tail = newNode;
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
