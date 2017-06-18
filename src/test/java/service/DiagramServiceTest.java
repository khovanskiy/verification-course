package service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.diagram.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class DiagramServiceTest {
    private DiagramService diagramService;

    @Before
    public void before() {
        diagramService = new DiagramService();
    }

    @Test
    public void readFromFile() throws Exception {
        Diagram diagram = diagramService.parseDiagram(new File("data/VarParser.xstd"));
        Assert.assertEquals("Widget number", 21, diagram.getWidget().size());
    }

    @Test
    @Ignore("Probably we not need in writing")
    public void write() throws IOException {
        Diagram etalon = new Diagram();
        Event event = new Event();
        List<Action> ts = Arrays.asList(new Action("a", null), new Action("b", null));
        TransitionAttributes transitionAttributes = new TransitionAttributes();
        transitionAttributes.setEvent(event);
        transitionAttributes.setActions(ts);
        TransitionWidget w = new TransitionWidget();
        etalon.setWidget(Arrays.asList(w));
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(new File("output.xml"), etalon);
    }
}
