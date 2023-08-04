package compiler;

import java.util.Hashtable;

class SymbolTable {

    private Hashtable table;

    public SymbolTable() {
        table = new Hashtable();
    }
    public int getSize() {
        return table.size();
    }

//for add item
    public void add(String key, Object value) {
        table.put(key, value);
    }
//for search
    public Object lookup(String sym) {
        for (int i = table.size() - 1; i >= 0; i--) {
            Object value = table.get(sym);
            if (value != null) return value;
        }
        return null;
    }
//to string
    public String toString() {
        String res = "";
        res = "Table is :";
        for (int i = 0 ; i < table.size() ; i++) {
            res += "\n" + "Symbol Scope " + table.toString() + "\n";
        }
        return res;
    }
}