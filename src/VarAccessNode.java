public class VarAccessNode extends Node{

    Token varNameToken;

    public VarAccessNode(Token varNameToken){
        this.varNameToken = varNameToken;
    }

    @Override
    public Token getToken() {
        return this.varNameToken;
    }
    
}
