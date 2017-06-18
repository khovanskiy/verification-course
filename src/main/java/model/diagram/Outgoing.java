package model.diagram;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "outgoing")
public class Outgoing {
    private Integer id;
}
