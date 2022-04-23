import java.util.List;

public class FuncDefNode extends Node{
    Token varNameToken;
    List<Token> argNameTokens;
    Node bodyNode;

    public FuncDefNode(Token varNameToken, List<Token> argNameTokens, Node bodyNode){
        this.varNameToken = varNameToken;
        this.argNameTokens = argNameTokens;
        this.bodyNode = bodyNode;
    }


    public Token getVarNameToken(){
        return this.varNameToken;
    }

    public List<Token> getArgNameToken(){
        return this.argNameTokens;
    }


    public Node getBodyNode(){
        return this.bodyNode;
    }

    @Override
    public Token getToken() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
