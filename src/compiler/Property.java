package compiler;

public class Property extends Variable{

    private Class initialValue;

    public Class getInitialValue() {
        return initialValue;
    }
    public void setInitialValue(Class initialValue) {
        this.initialValue = initialValue;
    }

}
