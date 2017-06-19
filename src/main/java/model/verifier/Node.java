package model.verifier;

import model.ltl.Formula;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node<T> {
    private final Set<Node<T>> incoming;
    private final Set<Formula<T>> old;
    private final Set<Formula<T>> now;
    private final Set<Formula<T>> next;

    public Node() {
        old = new LinkedHashSet<>();
        now = new LinkedHashSet<>();
        next = new LinkedHashSet<>();
        incoming = new LinkedHashSet<>();
    }

    public Node(Node<T> node) {
        this.old = new LinkedHashSet<>(node.old);
        this.incoming = new LinkedHashSet<>(node.incoming);
        this.now = new LinkedHashSet<>(node.now);
        this.next = new LinkedHashSet<>(node.next);
    }

    public Set<Node<T>> getIncoming() {
        return incoming;
    }

    public Set<Formula<T>> getOld() {
        return old;
    }

    public Set<Formula<T>> getNow() {
        return now;
    }

    public Set<Formula<T>> getNext() {
        return next;
    }
}
