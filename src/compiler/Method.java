package compiler;

import java.util.ArrayList;

public class Method {
    private String name;
    private ArrayList<Variable> arguments = new ArrayList<Variable>();
    private String returnValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Variable> getArguments() {
        return arguments;
    }

    public void setArgument(Variable argument) {
        this.arguments.add(argument);
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
    public Boolean equalsA(Variable variable){
        for(int i = 0 ; i < this.getArguments().size() ; i++){
            if(this.getArguments().get(i).equals(variable)){
                return true;
            }
        }
        return false;
    }

}
