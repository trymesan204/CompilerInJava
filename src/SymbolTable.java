import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    Map<String, String> symbols = new HashMap<>();

    public SymbolTable(){

    }

    public String get(String name){
        String value = symbols.get(name);
        return value;
    }

    public void printAll(){
        for ( String name: symbols.keySet()){
            System.out.println(name+" "+symbols.get(name));
        }
    }

    public void set(String name, String value){
        symbols.put(name, value);
    }

    public void remove(String name){
        symbols.remove(name);
    }

    
    
}
