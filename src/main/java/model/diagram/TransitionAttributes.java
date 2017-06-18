package model.diagram;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
public class TransitionAttributes extends Attributes {
    private Event event;
    private List<Action> actions;
    private String code;
    private String guard;

    @JacksonXmlProperty(localName = "action")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Action> getActions() {
        return actions;
    }
}
