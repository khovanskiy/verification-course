package model.diagram;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Widget {
    private Integer id;
    private String type;
    private Attributes attributes;

    public Widget(String type) {
        this.type = type;
    }

    @JacksonXmlProperty(isAttribute = true)
    public Integer getId() {
        return id;
    }
}
