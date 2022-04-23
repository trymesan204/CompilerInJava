public class NumberNode extends Node{
    Token token;
    public NumberNode(Token token){
        this.token = token;
    }

    public Token getToken(){
        return this.token;
    }
    
}
