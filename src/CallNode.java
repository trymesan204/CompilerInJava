import java.util.List;

public class CallNode extends Node{
    Node nodeToCall;
    List<Node> argNodes;

    public CallNode(Node nodeToCall, List<Node> argNodes){
        this.nodeToCall = nodeToCall;
        this.argNodes = argNodes;
    }

    public Node getNodeToCall(){
        return this.nodeToCall;
    }

    public List<Node> argNodes(){
        return this.argNodes;
    }

    @Override
    public Token getToken() {
        // TODO Auto-generated method stub
        return null;
    }

}
