import java.util.List;

public class ListNode extends Node {
    List<Node> elementNodes;
    public ListNode(List<Node> elementNodes){
        this.elementNodes = elementNodes;
    }

    public List<Node> getElementNodes(){
        return this.elementNodes;
    }

    @Override
    public Token getToken() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
