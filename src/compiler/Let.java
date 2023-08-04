package compiler;

import java.util.ArrayList;

public class Let {

    private Let Parent;
    private ArrayList<Property> arguments = new ArrayList<Property>();
    public static int letNumber = 0;

    public Let() {
        letNumber++;
    }

    public ArrayList<Property> getArguments() {
        return arguments;
    }

    public void setArguments(Property argument) {
        this.arguments.add(argument);
    }

    public boolean checkArguments(String property){
        for(int i = 0 ; i < this.getArguments().size(); i++){
            if(this.getArguments().get(i).getName().equals(property)){
                return true;
            }
        }
        return false;
    }

    public Let getParent() {
        return Parent;
    }

    public void setParent(Let parent) {
        Parent = parent;
    }

    public static int getLetNumber() {
        return letNumber;
    }

    public static void setLetNumber(int letNumber) {
        Let.letNumber = letNumber;
    }

}
