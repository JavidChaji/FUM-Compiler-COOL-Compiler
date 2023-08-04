package compiler;

import java.util.ArrayList;

public class Class {
    public Class() {

    }
    private String name;
    private String parent;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
