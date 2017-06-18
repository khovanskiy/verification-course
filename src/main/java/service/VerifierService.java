package service;

import lombok.RequiredArgsConstructor;
import model.buchi.Automaton;
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
        node.getNovel().add(ltl);
        return expand(node, new LinkedHashSet<>());
    }

    public <T> Iterable<T> check(Automaton<T> model, Formula<T> ltl) {
        Node<T> init = new Node<>();
        ltl = ltl.toNormalForm(true);
        Node<T> node = new Node<>();
        node.getIncoming().add(init);
        node.getNovel().add(ltl);
        Set<Node<T>> automaton = expand(node, new LinkedHashSet<>());
        Automaton<T> ltlAutomaton = transform(init, automaton);
        Automaton<T> badLang = automatonService.intersect(ltlAutomaton, model);
        return badLang.findAWord();
    }

    private <T> Set<Node<T>> expand(Node<T> v, Set<Node<T>> nodes) {
        if (v.getNovel().isEmpty()) {
            for (Node<T> r : nodes) {
                if (r.getOld().equals(v.getOld()) && r.getNext().equals(v.getNext())) {
                    r.getIncoming().addAll(v.getIncoming());
                    return nodes;
                }
            }
            Node<T> newNode = new Node<>();
            newNode.getIncoming().add(v);
            newNode.getNovel().addAll(v.getNext());
            nodes.add(v);
            return expand(newNode, nodes);
        } else {
            Formula<T> n = v.getNovel().iterator().next();
            v.getNovel().remove(n);
            if (v.getOld().contains(n)) {
                return expand(v, nodes);
            }
            if (v.getOld().contains(n.negation())) {
                return nodes;
            }
            Visitor<T> visitor = new Visitor<>(v, nodes);
            n.accept(visitor);
            return visitor.result;
        }
    }

    private <T> Set<Node<T>> replace(Node<T> current, Set<Node<T>> nodes, Formula<T> n) {
        Node<T> newNode = new Node<>(current);
        newNode.getOld().add(n);
        return expand(newNode, nodes);
    }

    private <T> Automaton<T> transform(Node<T> init, Set<Node<T>> old) {
        //TODO
        return null;
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
                current.getNovel().add(binary.getLeft());
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNovel().add(binary.getRight());
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if (binary.getOperation() == AND) {
                Node<T> newNode = new Node<>(current);
                newNode.getOld().add(binary);
                newNode.getNovel().addAll(asList(binary.getLeft(), binary.getRight()));
                result = expand(newNode, nodes);
            }
            if (binary.getOperation() == R) {
                Node<T> newNode1 = new Node<>(current);
                newNode1.getOld().add(binary);
                newNode1.getNovel().add(binary.getLeft());
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNovel().addAll(asList(binary.getLeft(), binary.getRight()));
                newNode2.getNext().add(LTL.release(binary.getLeft(), binary.getRight()));
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if (binary.getOperation() == U) {
                Node<T> newNode1 = new Node<>(current);
                newNode1.getOld().add(binary);
                newNode1.getNovel().add(binary.getLeft());
                newNode1.getNext().add(LTL.until(binary.getLeft(), binary.getRight()));
                Node<T> newNode2 = new Node<>(current);
                newNode2.getOld().add(binary);
                newNode2.getNovel().add(binary.getRight());
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
            result = replace(current, nodes, formula);
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