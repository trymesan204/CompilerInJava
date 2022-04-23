import java.util.ArrayList;
import java.util.List;

public class IfNode extends Node{
    List<List<Node>> cases = new ArrayList<>();
    Node elseCase;

    public IfNode( List<List<Node>> cases, Node elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
    }

    public List<List<Node>> getCases(){
        return this.cases;
    }

    public Node getElseCase(){
        return this.elseCase;
    }

    @Override
    public Token getToken() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
