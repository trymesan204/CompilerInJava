public class VarAssignNode extends Node {
    Token varNameToken;
    String valueNode;

    public VarAssignNode(Token varNameToken, String valueNode){
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;
    }

    public String getValueNode(){
        return this.valueNode;
    }

    @Override
    public Token getToken() {
        return this.varNameToken;
    }
    
}
