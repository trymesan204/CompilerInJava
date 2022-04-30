import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Assembly {
    List<Token> tokens = new ArrayList<>();
    Token operator;
    List<Token> registers = new ArrayList<>();
    List<Token> trueConditions = new ArrayList<>();
    List<Token> falseConditions = new ArrayList<>();
    List<Token> exitConditions = new ArrayList<>();
    List<Token> ifConditions = new ArrayList<>();
    List<Token> whileConditions = new ArrayList<>();
    List<Token> endLoopConditions = new ArrayList<>();
    List<Token> exitFunction = new ArrayList<>();


    public Assembly(List<Token> tokens){
        this.tokens = tokens;
        setLists();
    }

    private void setLists(){
        for ( int i = 0; i < 8; i++){
            registers.add(new Token(TokenType.REGISTERS,"T"+i));
            trueConditions.add(new Token(TokenType.CONDITIONS, "@TRUE"+i));
            falseConditions.add(new Token(TokenType.CONDITIONS, "@FALSE"+i));
            exitConditions.add(new Token(TokenType.CONDITIONS,"@EXIT"+i));
            ifConditions.add(new Token(TokenType.CONDITIONS,"@if"+i));
            whileConditions.add(new Token(TokenType.CONDITIONS,"@while"+i));
            endLoopConditions.add( new Token(TokenType.CONDITIONS,"@EXIT"+i));
            exitFunction.add(new Token(TokenType.CONDITIONS,"@EXITF"+ i));
        }
    }

    private int locateOperator(){
        for ( int i = 0; i < tokens.size(); i++){
            Token currentToken = tokens.get(i);
            operator = currentToken;
            switch(currentToken.type){
                case PLUS:
                    return i;
                case MINUS:
                    return i;
                case MUL:
                    return i;
                case DIV:
                    return i;
                case EQUALS:
                    return i;
                case LESSTHAN:
                case LESSTHANOREQUALSTO:
                case GREATERTHAN:
                case GREATERTHANOREQUALSTO:
                case EQUALSEQUALS:
                case NOTEQUALS:
                    return i;
                case KEYWORD:
                    if(currentToken.value.equals("if"))
                        return i;
                    if(currentToken.value.equals("else"))
                        return i;
                    if(currentToken.value.equals("elif"))
                        return i;
                    if(currentToken.value.equals("end"))
                        return i;
                    if(currentToken.value.equals("while"))
                        return i;
                    if(currentToken.value.equals("endif"))
                        return i;
                    if(currentToken.value.equals("func"))
                        return i;
                    if(currentToken.value.equals("endf"))
                        return i;
                case IDENTIFIER:
                    if(tokens.get(i+1).type == TokenType.LEFTP){
                        return i;
                    }
                }
        }
        return -1;
    }

    private void removeToken(int position){
        tokens.remove(position+1);
        tokens.remove(position);
        tokens.remove(position-1);
    }

    public void createAssembly(){
        int operatorPosition;
        int registerIndex = 0;
        int trueIndex = 0;
        int falseIndex = 0;
        int exitIndex = 0;
        int ifIndex = 0;
        int whileIndex = 0;
        int endLoopIndex = 0;

        do{
            operatorPosition = locateOperator();
            if(operatorPosition == -1 ) break;
            switch(operator.type){
                case PLUS:
                    System.out.println("mov ax, " + tokens.get(operatorPosition - 2).getValue());
                    System.out.println("add ax, " + tokens.get(operatorPosition - 1).getValue());
                    System.out.println("mov " + registers.get(registerIndex).getValue() + ", ax");
                    break;
                case MINUS:
                    System.out.println("mov ax, " + tokens.get(operatorPosition - 2).getValue());
                    System.out.println("sub ax, " + tokens.get(operatorPosition - 1).getValue());
                    System.out.println("mov " + registers.get(registerIndex).getValue() + ", ax");
                    break;
                case MUL:
                    System.out.println("mov ax, " + tokens.get(operatorPosition - 2).getValue());
                    System.out.println("mul ax, " + tokens.get(operatorPosition - 1).getValue());
                    System.out.println("mov " + registers.get(registerIndex).getValue() + ", ax");
                    break;
                case DIV:
                    System.out.println("mov ax, " + tokens.get(operatorPosition - 2).getValue());
                    System.out.println("div ax, " + tokens.get(operatorPosition - 1).getValue());
                    System.out.println("mov " + registers.get(registerIndex).getValue() + ", ax");
                    break;
                case EQUALS:
                    System.out.println("mov " + tokens.get(operatorPosition - 1).getValue() + ", " + tokens.get(operatorPosition - 2).getValue());
                    break;
                case LESSTHAN:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JGE "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case LESSTHANOREQUALSTO:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JG "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case GREATERTHAN:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JLE "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case GREATERTHANOREQUALSTO:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JL "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case EQUALSEQUALS:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JNE "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case NOTEQUALS:
                    System.out.println("CMP "+tokens.get(operatorPosition - 2).getValue()+", "+tokens.get(operatorPosition - 1).getValue());
                    System.out.println("JE "+falseConditions.get(falseIndex).getValue()+'\n');
                    System.out.println(trueConditions.get(trueIndex++).value);
                    break;
                case KEYWORD:
                    if(operator.value.equals("if")){
                        System.out.println(ifConditions.get(ifIndex++).value);
                        exitIndex = ifIndex - 1;
                    }else if(operator.value.equals("elif")){
                        System.out.println(exitConditions.get(exitIndex).value+'\n');
                        System.out.println(falseConditions.get(falseIndex++).value);
                    }else if(operator.value.equals("else")){
                        System.out.println(exitConditions.get(exitIndex).value+'\n');
                        System.out.println(falseConditions.get(falseIndex++).value);
                    }else if(operator.value.equals("endif")){
                        if(falseIndex == ifIndex)
                            System.out.println('\n'+falseConditions.get(exitIndex).value);
                        System.out.println('\n'+exitConditions.get(exitIndex).value+'\n');
                        exitIndex = exitIndex - 1;
                    }else if(operator.value.equals("while")){
                        System.out.println(whileConditions.get(whileIndex++).value);
                        endLoopIndex = whileIndex - 1;
                    }else if(operator.value.equals("end")){
                        System.out.println("JMP "+whileConditions.get(endLoopIndex).value);
                        System.out.println('\n'+falseConditions.get(falseIndex++).value);
                        System.out.println('\n'+endLoopConditions.get(endLoopIndex).value+'\n');
                        endLoopIndex = endLoopIndex - 1;
                    }else if(operator.value.equals("func")){
                        System.out.println("JMP @EXITF");
                        System.out.println("@"+tokens.get(operatorPosition+1).value);
                        if(tokens.get(operatorPosition + 2).type == TokenType.LEFTP){
                            int index = operatorPosition + 3;
                            while(tokens.get(index).type != TokenType.RIGHTP){
                                System.out.println("mov " + tokens.get(index).value + ", pop");
                                index++;
                            }

                            for (int i = operatorPosition + 1; i < index; i++){
                                tokens.remove(i);
                            }
                        }
                    }else if(operator.value.equals("endf")){
                        System.out.println("JMP @CALL");
                        System.out.println("@EXITF\n");
                    }
                    break;
                case IDENTIFIER:
                    if(tokens.get(operatorPosition + 1).type == TokenType.LEFTP){
                        int index = operatorPosition + 2;
                        Stack<Token> stack = new Stack<>();

                        while(index < tokens.size()){
                            if(tokens.get(index).type == TokenType.RIGHTP)
                                break;

                            stack.add(tokens.get(index));

                            index++;
                        }

                        for (int i = operatorPosition; i < index; i++){
                            tokens.remove(operatorPosition);
                        }

                        while(!stack.empty()){
                            System.out.println("push "+stack.pop().getValue());
                        }
                        System.out.println("JMP @"+operator.getValue());
                        System.out.println("@CALL");
                    }
                    break;

            }

            if(operator.type == TokenType.KEYWORD){
                tokens.remove(operatorPosition);
            }else if (operator.type == TokenType.PLUS ||
                      operator.type == TokenType.MINUS ||
                      operator.type == TokenType.MUL ||
                      operator.type == TokenType.DIV ){
                tokens.add(operatorPosition-2, registers.get(registerIndex));
                registerIndex++;
                
                removeToken(operatorPosition);
            }else{
                tokens.add(operatorPosition - 2, registers.get(registerIndex));
                removeToken(operatorPosition);
            }
            
        }while(tokens.size() >= 3 );

    }


}
