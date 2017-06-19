package service;

import lombok.RequiredArgsConstructor;
import model.ltl.*;
import model.verifier.Node;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static model.ltl.BinaryOperation.*;

public class VerifierService {
    private final AutomatonService automatonService;

    public VerifierService(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Create the set of nodes which represents buchi automaton.
     *
     * @param ltl the ltl-formula
     * @return the set of nodes
     */
    public <T> Set<Node<T>> create(Formula<T> ltl) {
        Node<T> init = new Node<>();
        ltl = ltl.toNormalForm(true);
        Node<T> node = new Node<>();
        node.getIncoming().add(init);
        node.getNow().add(ltl);
        Set<Node<T>> nodes = expand(node, new LinkedHashSet<>());
        node = null;
        return null;
    }

    private <T> Set<Node<T>> expand(Node<T> curr, Set<Node<T>> nodes) {
        if (curr.getNow().isEmpty()) {
            for (Node<T> q : nodes) {
                if (q.getNow().equals(curr.getNow()) && q.getNext().equals(curr.getNext())) {
                    q.getIncoming().addAll(curr.getIncoming());
                    return nodes;
                }
            }
            Node<T> newNode = new Node<>();
            newNode.getIncoming().add(curr);
            newNode.getNow().addAll(curr.getNext());
            nodes.add(curr);
            return expand(newNode, nodes);
        } else {
            Formula<T> n = curr.getNow().iterator().next();
            curr.getNow().remove(n);
            if (curr.getOld().contains(n)) {
                return expand(curr, nodes);
            }
            Visitor<T> visitor = new Visitor<>(curr, nodes);
            n.accept(visitor);
            return visitor.result;
        }
    }

    private <T> Set<Node<T>> replace(Node<T> current, Set<Node<T>> nodes, Formula<T> n) {
        Node<T> newNode = new Node<>(current);
        newNode.getOld().add(n);
        return expand(newNode, nodes);
    }

    @RequiredArgsConstructor
    private class Visitor<T> implements LTLVisitor<T> {
        private final Node<T> current;
        private final Set<Node<T>> nodes;
        private Set<Node<T>> result;

        @Override
        public void visit(BinaryFormula<T> binary) {
            if (binary.getOperation() == OR) {
                Node<T> newNode1 = new Node<>(current);
                current.getOld().add(binary);
                current.getNow().add(binary.getLeft());
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNow().add(binary.getRight());
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if (binary.getOperation() == AND) {
                Node<T> newNode = new Node<>(current);
                newNode.getOld().add(binary);
                newNode.getNow().addAll(asList(binary.getLeft(), binary.getRight()));
                result = expand(newNode, nodes);
            }
            if (binary.getOperation() == R) {
                Node<T> newNode1 = new Node<>(current);
                newNode1.getOld().add(binary);
                newNode1.getNow().addAll(asList(binary.getLeft(), binary.getRight()));
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNow().add(binary.getRight());
                newNode2.getNext().add(LTL.release(binary.getLeft(), binary.getRight()));
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if (binary.getOperation() == U) {
                Node<T> newNode1 = new Node<>(current);
                newNode1.getOld().add(binary);
                newNode1.getNow().add(binary.getLeft());
                newNode1.getNext().add(LTL.until(binary.getLeft(), binary.getRight()));
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNow().add(binary.getRight());
                result = expand(newNode2, expand(newNode1, nodes));
            }
        }

        @Override
        public void visit(Const<T> c) {
            if (c.isTrue()) {
                result = replace(current, nodes, c);
            } else {
                result = nodes;
            }
        }

        @Override
        public void visit(Not<T> formula) {
            if(current.getOld().contains(formula.negation())){
                result = nodes;
            } else {
                result = replace(current, nodes, formula);
            }
        }

        @Override
        public void visit(Next<T> formula) {
            Node<T> newNode = new Node<>(current);
            newNode.getOld().add(formula);
            newNode.getNext().add(formula.getF());
            result = expand(newNode, nodes);
        }

        @Override
        public void visit(Variable<T> variable) {
            result = replace(current, nodes, variable);
        }
    }
}