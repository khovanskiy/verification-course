package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class DiagramService {
    private XmlMapper xmlMapper;

    public DiagramService() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public Diagram parseDiagram(File file) throws IOException {
        Diagram diagram = xmlMapper.readValue(file, Diagram.class);
        return prepareDiagram(diagram);
    }

    public Diagram parseDiagram(String xml) throws IOException {
        Diagram diagram = xmlMapper.readValue(xml, Diagram.class);
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
}
