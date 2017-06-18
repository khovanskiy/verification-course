package model.diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Setter;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
//@Getter
@Setter
@JacksonXmlRootElement(localName = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private StateMachine stateMachine;

    @JacksonXmlProperty(localName = "Statemachine")
    public StateMachine getStateMachine() {
        return stateMachine;
    }
}
