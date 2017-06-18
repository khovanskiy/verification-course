package model.diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "event")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String name;
    private String comment;

    @JacksonXmlProperty(isAttribute = true)
    public String getName() {
        return name;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getComment() {
        return comment;
    }
}
