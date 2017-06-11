package model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
public class ParserTest {

    private final String xml = "" +
            "<widget id=\"4\" type=\"Transition\" >\n" +
            "    <attributes>\n" +
            "      <event name=\"number\" comment=\"123\" />\n" +
            "      <action name=\"SetID1\" comment=\"\"/>\n" +
            "      <action name=\"SetID2\" comment=\"\"/>\n" +
            "      <action name=\"SetID3\" comment=\"\"/>\n" +
            "      <code/>\n" +
            "      <guard/>\n" +
            "    </attributes>\n" +
            "</widget>";

    @Test
    public void read() throws IOException, XMLStreamException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Diagram diagram = xmlMapper.readValue(new File("data/VarParser.xstd"), Diagram.class);
        diagram.toString();
        //Widget widget = xmlMapper.readValue(xml, Widget.class);
        //Assert.assertEquals("Actions number", 3, widget.getAttributes().getActions().size());
    }

    //@Test
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
