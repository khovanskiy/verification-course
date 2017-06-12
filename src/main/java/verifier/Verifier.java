package verifier;

import buchi.Automaton;
import lombok.RequiredArgsConstructor;
import ltl.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ltl.BinaryOperation.*;

public class Verifier<T> {
    private Automaton<T> model;

    public Verifier(Automaton<T> model){
        this.model = model;
    }

    public Iterable<T> check(Formula<T> ltl){
        ltl = ltl.toNormalForm(true);
        Node node = new Node();
        node.novel.add(ltl);
        Set<Node> automaton = expand(node, new LinkedHashSet<>());
        Automaton<T> ltlAutomaton = transform(automaton);
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
        Node newNode = new Node();
        newNode.incoming.addAll(current.incoming);
        newNode.old.addAll(current.old);
        newNode.old.add(n);
        newNode.novel.addAll(current.novel);
        newNode.next.addAll(current.next);
        return expand(newNode, nodes);
    }

    private Automaton<T> transform(Set<Node> old){

    }

    @RequiredArgsConstructor
    private class Visitor implements LTLVisitor<T> {
        private final Node current;
        private final Set<Node> nodes;
        private Set<Node> result;

        @Override
        public void visit(BinaryFormula<T> binary) {
            if(binary.getOperation() == OR){

            }
            if(binary.getOperation() == AND){

            }
            if(binary.getOperation() == R){

            }
            if(binary.getOperation() == U){

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

        }

        @Override
        public void visit(Variable<T> variable) {
            result = replace(current, nodes, variable);
        }
    }

    private class Node {
        List<Node> incoming;
        Set<Formula<T>> old;
        Set<Formula<T>> novel;
        Set<Formula<T>> next;

        Node(){
            old = new LinkedHashSet<>();
            novel = new LinkedHashSet<>();
            next = new LinkedHashSet<>();
            incoming = new ArrayList<>();
        }
    }
}