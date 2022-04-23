public class BinaryOperatorNode extends Node{
    Node left;
    Token token;
    Node right;

    public BinaryOperatorNode(Node left, Token token, Node right){
        this.left = left;
        this.token = token;
        this.right = right;
    }

    public Node getLeftNode(){
        return this.left;
    }

    public Token getToken(){
        return this.token;
    }

    public Node getRightNode(){
        return this.right;
    }

}
