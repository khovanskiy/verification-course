package model.verifier;

import model.ltl.Formula;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node<T> {
    private final Set<Node<T>> incoming;
    private final Set<Formula<T>> old;
    private final Set<Formula<T>> novel;
    private final Set<Formula<T>> next;

    public Node() {
        old = new LinkedHashSet<>();
        novel = new LinkedHashSet<>();
        next = new LinkedHashSet<>();
        incoming = new LinkedHashSet<>();
    }

    public Node(Node<T> node) {
        this.old = new LinkedHashSet<>(node.old);
        this.incoming = new LinkedHashSet<>(node.incoming);
        this.novel = new LinkedHashSet<>(node.novel);
        this.next = new LinkedHashSet<>(node.next);
    }

    public Set<Node<T>> getIncoming() {
        return incoming;
    }

    public Set<Formula<T>> getOld() {
        return old;
    }

    public Set<Formula<T>> getNovel() {
        return novel;
    }

    public Set<Formula<T>> getNext() {
        return next;
    }
}
