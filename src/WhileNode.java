public class WhileNode extends Node {
    Node condition;
    Node body;
    
    public WhileNode( Node condition, Node body){
        this.condition = condition;
        this.body = body;
    }

    public Node getCondition(){
        return this.condition;
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
