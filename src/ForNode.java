public class ForNode extends Node{
    Token varName;
    Node startValue, endValue, body;
    public ForNode(Token varName, Node startValue, Node endValue, Node body){
        this.varName = varName;
        this.startValue = startValue;
        this.endValue = endValue;
        this.body = body;
    }

    public Token getVarName(){
        return this.varName;
    }

    public Node getStartValue(){
        return this.startValue;
    }

    public Node getEndValue(){
        return this.endValue;
    }

    public Node getBody(){
        return this.body;
    }

    @Override
    public Token getToken() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
