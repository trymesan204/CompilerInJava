import java.util.HashMap;
import java.util.Map;

public class FunctionSymbolTable {
    Map<String, Node> symbols = new HashMap<>();

    public FunctionSymbolTable(){

    }

    public Node get(String name){
        Node function = symbols.get(name);
        return function;
    }

    public void set(String name, Node function){
        symbols.put(name, function);
    }

    public void remove(String name){
        symbols.remove(name);
    }
}
