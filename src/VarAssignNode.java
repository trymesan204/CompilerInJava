public class VarAssignNode extends Node {
    Token varNameToken;
    Node valueNode;

    public VarAssignNode(Token varNameToken, Node valueNode){
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;
    }

    public Node getValueNode(){
        return this.valueNode;
    }

    @Override
    public Token getToken() {
        return this.varNameToken;
    }
    
}
