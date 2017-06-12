package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = TransitionWidget.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StateWidget.class, name = "State"),
        @JsonSubTypes.Type(value = TransitionWidget.class, name = "Transition")
})*/
@Getter
@Setter
public class Widget {
    private Integer id;
    private String type;
    private Attributes attributes;

    @JacksonXmlProperty(isAttribute = true)
    public Integer getId() {
        return id;
    }
}
