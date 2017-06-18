package model.diagram;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "diagram")
public class Diagram {
    private String name;
    private Data data;
    @JacksonXmlProperty(localName = "widget")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Widget> widget;
}
