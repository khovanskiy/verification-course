package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@JacksonXmlRootElement(localName = "action")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Action {
    private String name;
    private String comment;

    public Action(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getName() {
        return name;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getComment() {
        return comment;
    }
}
