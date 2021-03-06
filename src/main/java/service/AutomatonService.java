package service;

import com.google.common.collect.HashBiMap;
import model.buchi.*;
import model.diagram.*;
import model.ltl.Formula;
import model.ltl.LTL;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Triple;
import util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class AutomatonService {
    private static final String PREFIX = "s";
    private final SystemService systemService;

    public AutomatonService(SystemService systemService) {
        this.systemService = systemService;
    }

    public <T> Automaton<Formula<T>> createFromLtl(Formula<T> ltl) {
        Map<T, String> variableMap = new HashMap<>();
        Formula<String> lowercaseLTL = ltl.map(k -> variableMap.computeIfAbsent(k, v -> "v" + variableMap.size()));
        String spin = lowercaseLTL.toString().replace("R", "V");
        Map<String, T> oldNames = HashBiMap.create(variableMap).inverse();
        Automaton<Formula<String>> automaton = createFromLtl(spin);
        return map(automaton, t -> t.map(oldNames::get));
    }

    public Automaton<Formula<String>> createFromLtl(String ltl) {
        List<State> states = createStateListFromLtl(ltl);
        AtomicInteger ids = new AtomicInteger();
        Map<String, Integer> idMap = new HashMap<>();
        Automaton<Formula<String>> automaton = new Automaton<>();
        for (State state : states) {
            int nodeId = idMap.computeIfAbsent(state.getName(), k -> ids.getAndIncrement());
            if (isAcceptance(state.getName())) {
                automaton.setAccepting(nodeId);
            }
            if (isInit(state.getName())) {
                automaton.setInitialState(nodeId);
            }
            for (Transition transition : state.getTransitions()) {
                int nextId = idMap.computeIfAbsent(transition.getStateName(), k -> ids.getAndIncrement());
                Formula<String> formula = transition.getExpression();
                automaton.addTransition(nodeId, nextId, formula);
                if (isAcceptance(transition.getStateName())) {
                    automaton.setAccepting(nextId);
                }
                if (isInit(transition.getStateName())) {
                    automaton.setInitialState(nextId);
                }
            }
        }
        for (int nodeId : automaton.getNodes()) {
            if (automaton.get(nodeId).isEmpty()) {
                automaton.addTransition(nodeId, nodeId, LTL.t());
            }
        }
        return automaton;
    }

    public List<State> createStateListFromLtl(String ltl) {
        String[] command = new String[]{"ltl2ba/ltl2ba.exe", "-f", ltl};
        return systemService.executeForRead(command, inputStream -> {
            try {
                String string = StreamUtils.toString(inputStream);
                CharStream in = CharStreams.fromString(string);
                BuchiLexer lexer = new BuchiLexer(in);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                BuchiParser parser = new BuchiParser(tokens);
                return parser.compilationUnit().list;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public boolean isAcceptance(String name) {
        return name.startsWith("accept_");
    }

    public boolean isInit(String name) {
        return name.endsWith("_init");
    }

    public <A, B> Automaton<B> map(Automaton<A> input, Function<A, B> mapper) {
        Automaton<B> output = new Automaton<>();
        for (Map.Entry<Integer, Map<A, List<Integer>>> entry : input.getAutomaton().entrySet()) {
            int nodeId = entry.getKey();
            Map<A, List<Integer>> successors = entry.getValue();
            for (Map.Entry<A, List<Integer>> i : successors.entrySet()) {
                A a = i.getKey();
                B b = mapper.apply(a);
                for (int nextId : i.getValue()) {
                    output.addTransition(nodeId, nextId, b);
                }
            }
        }
        for (int nodeId : input.getAcceptingSet()) {
            output.setAccepting(nodeId);
        }
        output.setInitialState(input.getInitialState());
        return output;
    }

    public List<State> parse(String str) {
        CharStream in = CharStreams.fromString(str);
        BuchiLexer lexer = new BuchiLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BuchiParser parser = new BuchiParser(tokens);
        return parser.compilationUnit().list;
    }

    public Automaton<Formula<String>> createFromDiagram(Diagram diagram) {
        Map<Integer, Widget> widgetMap = diagram.getWidget().stream().collect(Collectors.toMap(Widget::getId, Function.identity()));
        Map<Integer, Integer> successors = new HashMap<>();
        for (Widget widget : diagram.getWidget()) {
            if (widget instanceof StateWidget) {
                StateWidget state = (StateWidget) widget;
                if (state.getAttributes().getIncomings() == null) {
                    continue;
                }
                for (Incoming incoming : state.getAttributes().getIncomings()) {
                    Widget prev = widgetMap.get(incoming.getId());
                    if (prev instanceof TransitionWidget) {
                        successors.put(incoming.getId(), widget.getId());
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        Automaton<Formula<String>> automaton = new Automaton<>();
        AtomicInteger ids = new AtomicInteger(10000);
        Integer initId = null;
        for (Widget widget : diagram.getWidget()) {
            if (widget instanceof StateWidget) {
                StateWidget state = (StateWidget) widget;
                Integer stateNode = state.getId();
                if (State.INITIAL.equals(state.getAttributes().getType())) {
                    if (initId == null) {
                        initId = ids.getAndIncrement();
                        automaton.setInitialState(initId);
                    }
                    Formula<String> stateEdge = createFormulaFromState(state);
                    automaton.addTransition(initId, stateNode, stateEdge);
                }
                if (state.getAttributes().getOutgoings() == null) {
                    continue;
                }
                for (Outgoing outgoing : state.getAttributes().getOutgoings()) {
                    Widget next = widgetMap.get(outgoing.getId());
                    if (next instanceof TransitionWidget) {
                        // event
                        Formula<String> eventEdge = createFormulaFromEvent(next.getAttributes().getEvent());
                        Integer eventNodeId = ids.getAndIncrement();
                        automaton.addTransition(stateNode, eventNodeId, eventEdge);

                        Integer lastNodeId = eventNodeId;
                        // actions
                        if (next.getAttributes().getActions() != null && !next.getAttributes().getActions().isEmpty()) {
                            for (Action action : next.getAttributes().getActions()) {
                                Formula<String> actionEdge = createFormulaFromActions(action);
                                Integer actionNodeId = ids.getAndIncrement();
                                automaton.addTransition(lastNodeId, actionNodeId, actionEdge);
                                lastNodeId = actionNodeId;
                            }
                        }

                        // next state
                        Integer successorId = successors.get(outgoing.getId());
                        StateWidget nextState = (StateWidget) widgetMap.get(successorId);
                        Integer nextNode = nextState.getId();
                        Formula<String> nextEdge = createFormulaFromState(nextState);
                        automaton.addTransition(lastNodeId, nextNode, nextEdge);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        for (int nodeId : automaton.getNodes()) {
            automaton.setAccepting(nodeId);
            if (automaton.get(nodeId).isEmpty()) {
                automaton.addTransition(nodeId, nodeId, LTL.t());
            }
        }
        return automaton;
    }

    private Formula<String> createFormulaFromState(StateWidget state) {
        return LTL.var("S(" + state.getAttributes().getName() + ")");
    }

    private Formula<String> createFormulaFromEvent(Event event) {
        return LTL.var("E(" + event.getName() + ")");
    }

    private Formula<String> createFormulaFromActions(Action action) {
        return LTL.var("A(" + action.getName() + ")");
    }

    private Formula<String> createFormulaFromActions(List<Action> actions) {
        if (actions == null || actions.isEmpty()) {
            return LTL.t();
        }
        List<Formula<String>> strings = actions.stream()
                .map(this::createFormulaFromActions)
                .collect(Collectors.toList());
        Formula<String> current = strings.get(0);
        for (int i = 1; i < strings.size(); ++i) {
            current = LTL.and(current, strings.get(i));
        }
        return current;
    }

    public <T> void saveAsDot(Automaton<T> graph, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("digraph G {");
            writer.println("\trankdir = LR;");
            for (Map.Entry<Integer, Map<T, List<Integer>>> entry : graph.getAutomaton().entrySet()) {
                int nodeId = entry.getKey();
                Map<T, List<Integer>> succesors = entry.getValue();
                String shape;
                if (graph.isAccepting(nodeId)) {
                    shape = "doublecircle";
                } else {
                    shape = "circle";
                }
                writer.println("\t" + PREFIX + nodeId + "[shape=\"" + shape + "\"];");
                for (Map.Entry<T, List<Integer>> outgoing : succesors.entrySet()) {
                    T symbol = outgoing.getKey();
                    List<Integer> next = outgoing.getValue();
                    for (int nextId : next) {
                        writer.println("\t" + PREFIX + nodeId + " -> " + PREFIX + nextId + "[label=\"" + symbol + "\"]");
                    }
                }
            }
            writer.println("}");
        }
    }

    public <T> Automaton<T> intersect(Automaton<T> a, Automaton<T> b, Intersector<T> intersector) {
        Automaton<T> c = new Automaton<>();
        Map<Triple<Integer, Integer, Integer>, Integer> tr = new HashMap<>();
        // Build all transitions
        for (Map.Entry<Integer, Map<T, List<Integer>>> e1 : a.getAutomaton().entrySet()) {
            int i = e1.getKey();
            Map<T, List<Integer>> iState = e1.getValue();
            for (Map.Entry<Integer, Map<T, List<Integer>>> e2 : b.getAutomaton().entrySet()) {
                int j = e2.getKey();
                Map<T, List<Integer>> jState = e2.getValue();
                for (T iSymbol : iState.keySet()) {
                    for (T jSymbol : jState.keySet()) {
                        T t = intersector.intersect(iSymbol, jSymbol);
                        if (t == null) {
                            continue;
                        }
                        for (int is : iState.get(iSymbol)) {
                            for (int js : jState.get(jSymbol)) {
                                c.addTransition(convert(i, j, 0, tr), convert(is, js, a.isAccepting(i) ? 1 : 0, tr), t);
                                c.addTransition(convert(i, j, 1, tr), convert(is, js, a.isAccepting(j) ? 0 : 1, tr), t);
                            }
                        }
                    }
                }
            }
        }
        // Accepting set of the new automaton
        for (int accB : b.getAcceptingSet()) {
            for (int i : a.getAutomaton().keySet()) {
                c.setAccepting(convert(i, accB, 1, tr));
            }
        }
        c.setInitialState(convert(a.getInitialState(), b.getInitialState(), 0, tr));
        return c;
    }

    private static int convert(int stateA, int stateB, int flag, Map<Triple<Integer, Integer, Integer>, Integer> tr) {
        return tr.computeIfAbsent(new Triple<>(stateA, stateB, flag), k -> tr.size());
    }
}
