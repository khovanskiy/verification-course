package model.diagram;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Workaround for https://github.com/FasterXML/jackson-dataformat-xml/issues/242
 *
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
@Deprecated
public class Attributes {
    private String name;
    private Integer type;
    private List<Incoming> incomings;
    private List<Outgoing> outgoings;
    private Nested nested;
    private Event event;
    private List<Action> actions;
    private String code;
    private String guard;

    @JacksonXmlProperty(localName = "incoming")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Incoming> getIncomings() {
        return incomings;
    }

    @JacksonXmlProperty(localName = "outgoing")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Outgoing> getOutgoings() {
        return outgoings;
    }

    @JacksonXmlProperty(localName = "action")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Action> getActions() {
        return actions;
    }
}
