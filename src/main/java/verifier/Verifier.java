package verifier;

import buchi.Automaton;
import lombok.RequiredArgsConstructor;
import ltl.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static ltl.BinaryOperation.*;

public class Verifier<T> {
    private Automaton<T> model;

    public Verifier(Automaton<T> model){
        this.model = model;
    }

    public Iterable<T> check(Formula<T> ltl){
        Node init = new Node();
        ltl = ltl.toNormalForm(true);
        Node node = new Node();
        node.incoming.add(init);
        node.novel.add(ltl);
        Set<Node> automaton = expand(node, new LinkedHashSet<>());
        Automaton<T> ltlAutomaton = transform(init, automaton);
        Automaton<T> badLang = Automaton.intersect(ltlAutomaton, model);
        return badLang.findAWord();
    }

    private Set<Node> expand(Node v, Set<Node> nodes){
        if(v.novel.isEmpty()){
            for(Node r : nodes) {
                if (r.old.equals(v.old) && r.next.equals(v.next)) {
                    r.incoming.addAll(v.incoming);
                    return nodes;
                }
            }
            Node newNode = new Node();
            newNode.incoming.add(v);
            newNode.novel.addAll(v.next);
            nodes.add(v);
            return expand(newNode, nodes);
        } else {
            Formula<T> n = v.novel.iterator().next();
            v.novel.remove(n);
            if(v.old.contains(n)){
                return expand(v, nodes);
            }
            if(v.old.contains(n.negation())){
                return nodes;
            }
            Visitor visitor = new Visitor(v, nodes);
            n.accept(visitor);
            return visitor.result;
        }
    }

    private Set<Node> replace(Node current, Set<Node> nodes, Formula<T> n){
        Node newNode = new Node(current);
        newNode.old.add(n);
        return expand(newNode, nodes);
    }

    private Automaton<T> transform(Node init, Set<Node> old){
        //TODO
        return null;
    }

    @RequiredArgsConstructor
    private class Visitor implements LTLVisitor<T> {
        private final Node current;
        private final Set<Node> nodes;
        private Set<Node> result;

        @Override
        public void visit(BinaryFormula<T> binary) {
            if(binary.getOperation() == OR){
                Node newNode1 = new Node(current);
                current.old.add(binary);
                current.novel.add(binary.getLeft());
                Node newNode2 = new Node(current);
                newNode2.old.add(binary);
                newNode2.novel.add(binary.getRight());
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if(binary.getOperation() == AND){
                Node newNode = new Node(current);
                newNode.old.add(binary);
                newNode.novel.addAll(asList(binary.getLeft(), binary.getRight()));
                result = expand(newNode, nodes);
            }
            if(binary.getOperation() == R){
                Node newNode1 = new Node(current);
                newNode1.old.add(binary);
                newNode1.novel.add(binary.getLeft());
                Node newNode2 = new Node(current);
                newNode2.old.add(binary);
                newNode2.novel.addAll(asList(binary.getLeft(), binary.getRight()));
                newNode2.next.add(LTL.release(binary.getLeft(), binary.getRight()));
                result = expand(newNode2, expand(newNode1, nodes));
            }
            if(binary.getOperation() == U){
                Node newNode1 = new Node(current);
                newNode1.old.add(binary);
                newNode1.novel.add(binary.getLeft());
                newNode1.next.add(LTL.until(binary.getLeft(), binary.getRight()));
                Node newNode2 = new Node(current);
                newNode2.old.add(binary);
                newNode2.novel.add(binary.getRight());
                result = expand(newNode2, expand(newNode1, nodes));
            }
        }

        @Override
        public void visit(Const<T> c) {
            if(c.isTrue()){
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
            Node newNode = new Node(current);
            newNode.old.add(formula);
            newNode.next.add(formula.getF());
            result = expand(newNode, nodes);
        }

        @Override
        public void visit(Variable<T> variable) {
            result = replace(current, nodes, variable);
        }
    }

    private class Node {
        Set<Node> incoming;
        Set<Formula<T>> old;
        Set<Formula<T>> novel;
        Set<Formula<T>> next;

        Node(){
            old = new LinkedHashSet<>();
            novel = new LinkedHashSet<>();
            next = new LinkedHashSet<>();
            incoming = new LinkedHashSet<>();
        }

        Node(Node node){
            this.old = new LinkedHashSet<>(node.old);
            this.incoming = new LinkedHashSet<>(node.incoming);
            this.novel = new LinkedHashSet<>(node.novel);
            this.next = new LinkedHashSet<>(node.next);
        }
    }
}