package com.github.boybeak.walk;

import java.util.List;
import java.util.TimeZone;

public class Walk {

    private String version;
    private int size;
    private TimeZone timeZone;
    private List<Node> nodes;

    public String getVersion() {
        return version;
    }

    public int getSize() {
        return size;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tversion=" + version + ",\n" +
                "\tsize=" + size + ",\n" +
                "\ttimeZone=" + timeZone.getID() + ",\n" +
                "}";
    }
}