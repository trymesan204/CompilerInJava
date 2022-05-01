import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<Token> tokens;
    int index = -1;
    Token currentToken;
    int advanceCount = 0;
    List<Token> tokenAssem = new ArrayList<>();

    public Parser(List<Token> tokens, List<Token> tokenAssem){
        this.tokens = tokens;
        this.tokenAssem = tokenAssem;
        advance();
    }

    public Token advance(){
        advanceCount++;
        index++;
        if(index < tokens.size())
            currentToken = tokens.get(index);
        return currentToken;
    }

    public void updateCurrentToken(){
        index = index - 1;
        if(index >= 0 && index<tokens.size()){
            currentToken = tokens.get(index);
        }
    }

    public SyntaxTree parse(){
        SyntaxTree tree = statements();
        if( currentToken.getType() != TokenType.EOF){
            throw new InvalidSyntaxException();
        }
        return tree;
    }

    public Node ifExpression(){
        return ifExpressionCases("if");
    }

    public Node ifExpressionB(){
        return ifExpressionCases("elif");
    }

    public Node ifExpressionC(){
        Node elseCase = null;

        if(currentToken.matches(TokenType.KEYWORD, "else")){
            tokenAssem.add(currentToken);
            advance();

            if(currentToken.getType() == TokenType.NEWLINE){
                advance();

                Node statement = statements().getNode();
                elseCase = statement;

                if(currentToken.matches(TokenType.KEYWORD, "endif")){
                    tokenAssem.add(currentToken);
                    advance();
                }else{
                    throw new InvalidSyntaxException("Expected 'endif'");
                }
            }else{
                Node expression = expression().getNode();
                elseCase = expression;
            }
        }
        return elseCase;
    }

    public Node ifExpressionBOrC(){
        List<List<Node>> cases = new ArrayList<>();
        Node elseCase = null;

        if (currentToken.matches(TokenType.KEYWORD, "elif")){
            tokenAssem.add(currentToken);
            Node allCases = ifExpressionB();
            elseCase = ((IfNode)allCases).getElseCase();
            cases = ((IfNode)allCases).getCases();
        }else{
            elseCase = ifExpressionC();
        }
        return new IfNode(cases, elseCase);
    }

    public Node ifExpressionCases(String caseKeyword){
        List<List<Node>> cases = new ArrayList<>();
        Node elseCase = null;

        if(!currentToken.matches(TokenType.KEYWORD, caseKeyword)){
            new InvalidSyntaxException("Expected "+caseKeyword);
        }

        advance();

        Node condition = expression().getNode();

        if(!currentToken.matches(TokenType.KEYWORD, "then")){
            new InvalidSyntaxException("Expected then ");
        }

        advance();

        if(currentToken.getType() == TokenType.NEWLINE){
            advance();

            Node statement = statements().getNode();

            List<Node> condExp = new ArrayList<>();
            condExp.add(condition);
            condExp.add(statement);
            cases.add(condExp);

            if (currentToken.matches(TokenType.KEYWORD, "endif")){
                tokenAssem.add(currentToken);
                advance();
            }else{
                Node allCasesNode = ifExpressionBOrC();
                elseCase = ((IfNode)allCasesNode).getElseCase();
                List<List<Node>> allCases = ((IfNode)allCasesNode).getCases();
                for (List<Node> caseNode: allCases){
                    cases.add(caseNode);
                }
            }

        }else{
            Node expression = expression().getNode();
            List<Node> condExp = new ArrayList<>();
            condExp.add(condition);
            condExp.add(expression);
            cases.add(condExp);

            Node allCasesNode = ifExpressionBOrC();
            elseCase = ((IfNode)allCasesNode).getElseCase();
            List<List<Node>> allCases = ((IfNode)allCasesNode).getCases();
            for (List<Node> caseNode: allCases){
                cases.add(caseNode);
            }
        }

        return new IfNode(cases, elseCase);

    }

    public Node whileExpression(){
        if(!currentToken.matches(TokenType.KEYWORD, "while")){
            new InvalidSyntaxException("Expected while ");
        }

        advance();

        Node condition = expression().getNode();

        if(!currentToken.matches(TokenType.KEYWORD, "then")){
            new InvalidSyntaxException("Expected then ");
        }
        
        advance();

        if(currentToken.getType() == TokenType.NEWLINE){
            advance();

            Node body = statements().getNode();

            tokenAssem.add(currentToken);
            if(!currentToken.matches(TokenType.KEYWORD, "end")){
                new InvalidSyntaxException("Expected end ");
            }
            advance();

            return new WhileNode(condition, body);
        }

        Node body = expression().getNode();

        return new WhileNode(condition, body);
    }

    public Node forExpression(){
        if(!currentToken.matches(TokenType.KEYWORD, "for")){
            new InvalidSyntaxException("Expected for ");
        }

        advance();

        if (currentToken.getType() != TokenType.IDENTIFIER){
            new InvalidSyntaxException("Expected identifier");
        }

        Token varName = currentToken;
        advance();

        if (currentToken.getType() != TokenType.EQUALS){
            new InvalidSyntaxException("Expected '='");
        }

        advance();

        Node startValue = expression().getNode();

        if(!currentToken.matches(TokenType.KEYWORD, "to")){
            new InvalidSyntaxException("Expected to ");
        }

        advance();

        Node endValue = expression().getNode();

        if(!currentToken.matches(TokenType.KEYWORD, "then")){
            new InvalidSyntaxException("Expected then ");
        }

        advance();

        if(currentToken.getType() == TokenType.NEWLINE){
            advance();

            Node body = statements().getNode();

            if(!currentToken.matches(TokenType.KEYWORD, "end")){
                new InvalidSyntaxException("Expected end ");
            }
            advance();

            return new ForNode(varName, startValue, endValue, body);
        }

        Node body = expression().getNode();

        return new ForNode(varName, startValue, endValue, body);

    }

    public Node funcDef(){
        if(!currentToken.matches(TokenType.KEYWORD, "func")){
            new InvalidSyntaxException("Expected func ");
        }
        tokenAssem.add(currentToken);

        advance();
        
        if(currentToken.getType() != TokenType.IDENTIFIER){
            throw new InvalidSyntaxException("Expected identifier");
        }

        Token varNameToken = currentToken;
        tokenAssem.add(varNameToken);

        advance();

        tokenAssem.add(currentToken);
        if (!(currentToken.getType() == TokenType.LEFTP)){
            throw new InvalidSyntaxException("Expected (");
        }
        advance();

        List<Token> argNameTokens = new ArrayList<>();

        if(currentToken.getType() == TokenType.IDENTIFIER){
            argNameTokens.add(currentToken);
            tokenAssem.add(currentToken);

            advance();

            while (currentToken.getType() == TokenType.COMMA){
                advance();

                if(currentToken.getType() != TokenType.IDENTIFIER){
                    throw new InvalidSyntaxException("Expected identifier");
                }

                argNameTokens.add(currentToken);
                tokenAssem.add(currentToken);

                advance();
            }

            tokenAssem.add(currentToken);

            if (!(currentToken.getType() == TokenType.RIGHTP)){
                throw new InvalidSyntaxException("Expected ')' or ','");
            }
        }else{
            tokenAssem.add(currentToken);

            if (!(currentToken.getType() == TokenType.RIGHTP)){
                throw new InvalidSyntaxException("Expected ')'");
            }
        }

        advance();

        if(currentToken.getType() == TokenType.ARROW){
            
            advance();

            Node nodeToReturn = expression().getNode();

            return new FuncDefNode(varNameToken, argNameTokens, nodeToReturn);
        }

        if(currentToken.getType() != TokenType.NEWLINE){
            throw new InvalidSyntaxException("Expected -> or newline");
        }

        advance();

        Node body = statements().getNode();

        tokenAssem.add(currentToken);
        if(!currentToken.matches(TokenType.KEYWORD, "endf")){
            throw new InvalidSyntaxException("Expected endf");
        }

        advance();

        return new FuncDefNode(varNameToken, argNameTokens, body);
    }

    public Node listExpression(){
        List<Node> elementNodes = new ArrayList<>();

        if(currentToken.getType() != TokenType.LSQUARE){
            throw new InvalidSyntaxException("Expected '['");
        } 
        advance();

        if(currentToken.getType() == TokenType.RSQUARE){
            advance();
        }else{
            elementNodes.add(expression().getNode());
            while (currentToken.getType() == TokenType.COMMA){
                advance();
                elementNodes.add(expression().getNode());
            }
            if (currentToken.getType() != TokenType.RSQUARE){
                throw new InvalidSyntaxException("Expected ']'");
            }
            advance();
        }
        
        return new ListNode(elementNodes);
    }


    public Node atom(){
        Token token = currentToken;

        if (currentToken.getType() == TokenType.NUMBER){
            tokenAssem.add(currentToken);
            advance();
            return new NumberNode(token);
        }else if(currentToken.getType() == TokenType.IDENTIFIER){
            tokenAssem.add(currentToken);
            advance();
            return new VarAccessNode(token);
        }else if (currentToken.getType() == TokenType.LEFTP){
            advance();
            SyntaxTree expression = expression();
            if(currentToken.getType() == TokenType.RIGHTP){
                advance();
                return expression.getNode();
            }else{
                throw new InvalidSyntaxException("Expected ')'");
            }
        }else if(currentToken.getType() == TokenType.LSQUARE){
            Node listExpression = listExpression();
            return listExpression;
        }else if(currentToken.matches(TokenType.KEYWORD, "if")){
            tokenAssem.add(currentToken);
            Node ifExpr = ifExpression();
            return ifExpr;
        }else if (currentToken.matches(TokenType.KEYWORD, "while")){
            tokenAssem.add(currentToken);
            return whileExpression();
        }else if (currentToken.matches(TokenType.KEYWORD, "for")){
            tokenAssem.add(currentToken);
            return forExpression();
        }else if (currentToken.matches(TokenType.KEYWORD, "func")){
            return funcDef();
        }else if (currentToken.matches(TokenType.KEYWORD, "else") || 
                  currentToken.matches(TokenType.KEYWORD, "elif") ||
                  currentToken.matches(TokenType.KEYWORD, "endif") ||
                  currentToken.matches(TokenType.KEYWORD, "end") || 
                  currentToken.matches(TokenType.KEYWORD, "endf") ){
            return null;
        }
        throw new InvalidSyntaxException("Invalid token");
    }

    public Node call(){
        Node left = atom();
        List<Node> argNodeTokens = new ArrayList<>();
        if (currentToken.getType() == TokenType.LEFTP){
            tokenAssem.add(currentToken);
            advance();

            if (currentToken.getType() == TokenType.RIGHTP){
                advance();
            }else{
                argNodeTokens.add(expression().getNode());
                while (currentToken.getType() == TokenType.COMMA){
                    advance();
                    argNodeTokens.add(expression().getNode());
                }

                tokenAssem.add(currentToken);
                if (currentToken.getType() != TokenType.RIGHTP){
                    throw new InvalidSyntaxException("Expected ')'");
                }
                advance();
            }
            return new CallNode(left, argNodeTokens);
        }
        return left;
    }

    public Node powerExpression(){
        Node left = call();

        while (currentToken.getType() == TokenType.POWER){
            Token operatorToken = currentToken;
            advance();
            Node right = factor();
            left = new BinaryOperatorNode(left, operatorToken, right);
        }
        return left;
    }

    public Node factor(){
        return powerExpression();
    }


    public Node term(){
        Node left = factor();

        while ( currentToken.getType() == TokenType.MUL ||
                currentToken.getType() == TokenType.DIV)
        {
            Token operatorToken = currentToken;
            advance();
            Node right = factor();
            left = new BinaryOperatorNode(left, operatorToken, right);
            tokenAssem.add(operatorToken);
        }
        return left;
    }

    private Node arithmeticExpression(){
        Node left = term();

        while ( currentToken.getType() == TokenType.PLUS ||
                currentToken.getType() == TokenType.MINUS)
        {
            Token operatorToken = currentToken;
            advance();
            Node right = term();
            left = new BinaryOperatorNode(left, operatorToken, right);
            tokenAssem.add(operatorToken);
        }
        return left;
    }

    private Node comparisonExpression(){
        Node left = arithmeticExpression();

        while ( currentToken.getType() == TokenType.EQUALSEQUALS ||
                currentToken.getType() == TokenType.NOTEQUALS ||
                currentToken.getType() == TokenType.LESSTHAN ||
                currentToken.getType() == TokenType.GREATERTHAN ||
                currentToken.getType() == TokenType.GREATERTHANOREQUALSTO ||
                currentToken.getType() == TokenType.LESSTHANOREQUALSTO)
        {
            Token operatorToken = currentToken;
            advance();
            Node right = arithmeticExpression();
            left = new BinaryOperatorNode(left, operatorToken, right);
            tokenAssem.add(operatorToken);
        }
        
        return left;
    }

    public SyntaxTree expression(){
        advanceCount = 0;
        if(currentToken.matches(TokenType.KEYWORD, "var")){
            advance();

            if(currentToken.getType() != TokenType.IDENTIFIER){
                throw new InvalidSyntaxException("Expected an identifier");
            }
            Token varName = currentToken;
            advance();

            if(currentToken.getType() != TokenType.EQUALS){
                throw new InvalidSyntaxException("Expected '='");
            }
            advance();
            
            VarAssignNode node = new VarAssignNode(varName, expression().getNode());
            tokenAssem.add(varName);
            tokenAssem.add(new Token(TokenType.EQUALS));
            
            return new SyntaxTree(node);

        }else if (currentToken.getType() == TokenType.IDENTIFIER && tokens.get(index+1).getType() == TokenType.EQUALS ){
            Token varName = currentToken;
            
            advance();
            advance();

            VarAssignNode node = new VarAssignNode(varName, expression().getNode());
            return new SyntaxTree(node);

        }

        return new SyntaxTree(comparisonExpression());
    }

    public SyntaxTree statements(){
        
        List<Node> statements = new ArrayList<>();

        while (currentToken.getType() == TokenType.NEWLINE){
            advance();
        }

        Node statement = expression().getNode();
        statements.add(statement);


        boolean moreStatements = true;

        while (true){
            int newLineCount = 0;
            while (currentToken.getType() == TokenType.NEWLINE){
                advance();
                newLineCount++;
            }
            if (newLineCount == 0) moreStatements= false;

            if (!moreStatements) break;

            statement = expression().getNode();
            if(statement == null){
                moreStatements = false;
                continue;
            }
            statements.add(statement);
        }

        return new SyntaxTree(new ListNode(statements));

    }

    
}
