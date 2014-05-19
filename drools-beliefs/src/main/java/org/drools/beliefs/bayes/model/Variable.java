package org.drools.beliefs.bayes.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@XStreamAlias("VARIABLE")
public class Variable extends VariableXml implements Serializable {
    @XStreamAlias("NAME")
    private String name;

    @XStreamImplicit(itemFieldName = "OUTCOME")
    private List<String> outComes;

    @XStreamAlias("PROPERTY")
    private String position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOutComes() {
        return outComes;
    }

    public void setOutComes(List<String> outComes) {
        this.outComes = outComes;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

