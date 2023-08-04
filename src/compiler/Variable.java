package compiler;

public class Variable {
    private String name;
    private String type;

    public Variable() {
    }

    public Variable(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean equals(Variable variable){

        if(variable.name.equals(this.name) && variable.type.equals(this.type)){
            return true;
        }
        return false;
    }
    public Boolean equalsN(String variable){

        if(variable.equals(this.name)){
            return true;
        }
        return false;
    }

}
