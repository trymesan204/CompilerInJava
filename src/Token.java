public class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }

    public Token(TokenType type){
        this.type = type;
    }

    public TokenType getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

    public void print(){
        if(this.value != null){
            System.out.println(this.type+" "+this.value);
        }else{
            System.out.println(this.type);
        }
    }

    public boolean matches(TokenType type, String value){
        if(this.type == type && this.value.equals(value)){
            return true;
        }
        return false;
    }
}
