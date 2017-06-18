package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.diagram.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class DiagramService {
    private static final String PREFIX = "s";
    private XmlMapper xmlMapper;

    public DiagramService() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public Diagram parseDiagram(File file) throws IOException {
        Diagram diagram = xmlMapper.readValue(file, Diagram.class);
        return prepareDiagram(diagram);
    }

    private Diagram prepareDiagram(Diagram diagram) {
        List<Widget> widgets = Optional.ofNullable(diagram)
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .flatMap(d -> d.getWidget().stream())
                .map(w -> {
                    switch (w.getType()) {
                        case "State": {
                            StateWidget mapped = new StateWidget();
                            mapped.setId(w.getId());
                            mapped.setAttributes(new StateAttributes());
                            mapped.getAttributes().setName(w.getAttributes().getName());
                            mapped.getAttributes().setType(w.getAttributes().getType());
                            mapped.getAttributes().setIncomings(w.getAttributes().getIncomings());
                            mapped.getAttributes().setOutgoings(w.getAttributes().getOutgoings());
                            return mapped;
                        }
                        case "Transition": {
                            TransitionWidget mapped = new TransitionWidget();
                            mapped.setId(w.getId());
                            mapped.setAttributes(new TransitionAttributes());
                            mapped.getAttributes().setEvent(w.getAttributes().getEvent());
                            mapped.getAttributes().setActions(w.getAttributes().getActions());
                            mapped.getAttributes().setCode(w.getAttributes().getCode());
                            mapped.getAttributes().setGuard(w.getAttributes().getGuard());
                            return mapped;
                        }
                    }
                    return w;
                })
                .collect(Collectors.toList());
        diagram.setWidget(widgets);
        return diagram;
    }

    public Diagram parseDiagram(String xml) throws IOException {
        Diagram diagram = xmlMapper.readValue(xml, Diagram.class);
        return prepareDiagram(diagram);
    }

    /**
     * Convert the given diagram to .dot file
     * You should use Graphviz program to render .dot file to pdf or image using the following line:
     * $ dot -Gsplines=true -Goverlap=false -Tpdf graph.dot -o graph.pdf
     *
     * @param diagram the diagram
     * @param file    the output file
     * @throws IOException if io exception occurs
     */
    public void convertDiagramToDot(Diagram diagram, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            String graphName = diagram.getName();
            writer.println("digraph " + graphName + " {");
            writer.println("\trankdir = LR;");
            for (Widget widget : diagram.getWidget()) {
                if (widget instanceof StateWidget) {
                    StateWidget state = (StateWidget) widget;
                    writer.println("\t" + PREFIX + widget.getId() + " [label=\"" + state.getAttributes().getName() + "\"]");
                    if (state.getAttributes().getIncomings() != null) {
                        for (Incoming incoming : state.getAttributes().getIncomings()) {
                            writer.println("\t" + PREFIX + incoming.getId() + " -> " + PREFIX + state.getId() + ";");
                        }
                    }
                    if (state.getAttributes().getOutgoings() != null) {
                        for (Outgoing outgoing : state.getAttributes().getOutgoings()) {
                            writer.println("\t" + PREFIX + state.getId() + " -> " + PREFIX + outgoing.getId() + ";");
                        }
                    }
                } else if (widget instanceof TransitionWidget) {
                    TransitionWidget transition = (TransitionWidget) widget;
                    writer.println("\t" + PREFIX + widget.getId() + " [shape=\"none\", label=<" + getLabelByTransition(transition) + ">]");
                }
            }
            writer.println("}");
        }
    }

    private String getLabelByTransition(TransitionWidget transition) {
        TransitionAttributes attr = transition.getAttributes();
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr><td>Event</td><td>").append(attr.getEvent().getName()).append("</td></tr>");
        sb.append("<tr><td colspan=\"2\">").append(attr.getEvent().getComment()).append("</td></tr>");
        if (attr.getActions() != null) {
            sb.append("<tr><td colspan=\"2\">Actions:</td></tr>");
            List<Action> actions = attr.getActions();
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(i);
                sb.append("<tr><td>").append(i + 1).append(".</td><td>").append(action.getName()).append("</td></tr>");
            }
        }
        if (attr.getGuard() != null) {
            String guard = attr.getGuard().replace("&", "&amp;");
            sb.append("<tr><td>Guard</td><td><font color=\"red\">").append(guard).append("</font></td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
