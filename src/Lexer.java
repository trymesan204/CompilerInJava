import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String text;
    private int position = -1;
    private char currentChar;

    public Lexer(String text){
        this.text = text;
        advance();
    }

    private void advance(){
        position++;
        if (position < text.length())
            currentChar = text.charAt(position);
        else
            currentChar = '\0';
    }

    public List<Token> makeTokens(){
        List<Token> tokens = new ArrayList<>();

        while( currentChar != '\0'){

            if (currentChar == ' '){
                advance();
            }else if (currentChar == '+'){
                tokens.add(new Token(TokenType.PLUS));
                advance();
            }else if (currentChar == '-'){
                tokens.add(makeMinusOrArrow());
            }else if (currentChar == '*'){
                tokens.add(new Token(TokenType.MUL));
                advance();
            }else if (currentChar == '/'){
                tokens.add(new Token(TokenType.DIV));
                advance();
            }else if (currentChar == '('){
                tokens.add(new Token(TokenType.LEFTP));
                advance();
            }else if (currentChar == ')'){
                tokens.add(new Token(TokenType.RIGHTP));
                advance();
            }else if (currentChar == '['){
                tokens.add(new Token(TokenType.LSQUARE));
                advance();
            }else if (currentChar == ']'){
                tokens.add(new Token(TokenType.RSQUARE));
                advance();
            }else if(currentChar == '^'){
                tokens.add(new Token(TokenType.POWER));
                advance();
            }else if(currentChar == '='){
                tokens.add(makeEquals());
            }else if(currentChar == '<'){
                tokens.add(makeLessThan());
            }else if(currentChar == '>'){
                tokens.add(makeGreaterThan());
            }else if(currentChar == '!'){
                tokens.add(makeNotEquals());
            }else if(currentChar == ','){
                tokens.add(new Token(TokenType.COMMA));
                advance();
            }else if(currentChar == ';' || currentChar =='\n'){
                tokens.add(new Token(TokenType.NEWLINE));
                advance();
            }else if (Character.isDigit(currentChar)){
                tokens.add(makeNumber());
            }else if (Character.isLetter(currentChar)){
                tokens.add(makeWord());
            }else{
                advance();
                throw new TokenTypeException();
            }
        }

        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }

    public Token makeNumber(){
        int start = position;
        while ( Character.isDigit(currentChar) && currentChar != '\0' ){
            advance(); 
        }
        String number = text.substring(start, position); 
        return new Token(TokenType.NUMBER, number);
    }

    private Token makeWord(){
        int start = position;
        while ( Character.isLetter(currentChar) && currentChar != '\0' ){
            advance(); 
        }
        String word = text.substring(start, position); 
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("var");
        keywords.add("if");
        keywords.add("then");
        keywords.add("elif");
        keywords.add("else");
        keywords.add("while");
        keywords.add("func");
        keywords.add("for");
        keywords.add("to");
        keywords.add("end");
        if(keywords.contains(word)){
            return new Token(TokenType.KEYWORD, word);
        }else{
            return new Token(TokenType.IDENTIFIER, word);
        }
        
    }

    private Token makeNotEquals(){
        advance();
        if(currentChar == '='){
            advance();
            return new Token(TokenType.NOTEQUALS);
        }

        advance();
        throw new InvalidSyntaxException("Expected an '=' after '!'");
    }

    private Token makeEquals(){
        advance();

        if(currentChar == '='){
            advance();
            return new Token(TokenType.EQUALSEQUALS);
        }

        return new Token(TokenType.EQUALS);
    }

    private Token makeLessThan(){
        advance();

        if(currentChar == '='){
            advance();
            return new Token(TokenType.LESSTHANOREQUALSTO);
        }
        
        return new Token(TokenType.LESSTHAN);
    }

    private Token makeGreaterThan(){
        advance();

        if(currentChar == '='){
            advance();
            return new Token(TokenType.GREATERTHANOREQUALSTO);
        }
        
        return new Token(TokenType.GREATERTHAN);
    }

    public Token makeMinusOrArrow(){
        advance();

        if(currentChar == '>'){
            advance();
            return new Token(TokenType.ARROW);
        }

        return new Token(TokenType.MINUS);
    }
}