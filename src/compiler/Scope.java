package compiler;

public class Scope {

    public Scope() {
        //create symbolTable
        symbolTable = new SymbolTable();
    }

    Scope parent;
    SymbolTable symbolTable;
    int level;
}
