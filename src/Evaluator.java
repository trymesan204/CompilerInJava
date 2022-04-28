import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class Evaluator {
    Node root;
    SymbolTable symbolTable;
    FunctionSymbolTable functionSymbolTable;

    public Evaluator(Node root, SymbolTable symbolTable, FunctionSymbolTable functionSymbolTable){
        this.root = root;
        this.symbolTable = symbolTable;
        this.functionSymbolTable = functionSymbolTable;
    }

    public int evaluate(){
        return evaluateExpression(root);
    }

    private int evaluateExpression(Node root){
        if(root instanceof NumberNode){
            return Integer.parseInt(((NumberNode) root).getToken().getValue());
        }

        if (root instanceof BinaryOperatorNode){
            int left = evaluateExpression(((BinaryOperatorNode) root).getLeftNode());
            int right = evaluateExpression(((BinaryOperatorNode) root).getRightNode());
            

            if (((BinaryOperatorNode) root).getToken().getType() == TokenType.PLUS)
            {
                return left + right; 
            }
            else if (((BinaryOperatorNode) root).getToken().getType() == TokenType.MINUS)
            {
                
                return left - right;
            }
            else if (((BinaryOperatorNode) root).getToken().getType() == TokenType.MUL)
            {
                return left * right;
            }
            else if (((BinaryOperatorNode) root).getToken().getType() == TokenType.DIV)
            {
                if (right == 0){
                    throw new InvalidSyntaxException("Cannot Divide by zero");
                }else{
                    return left / right;
                }
                
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.POWER){
                return (int) Math.pow(left, right);
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.EQUALSEQUALS){
                return left == right?1:0;
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.NOTEQUALS){
                return left == right?0:1;
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.LESSTHAN){
                return left < right?1:0;
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.LESSTHANOREQUALSTO){
                return left <= right?1:0;
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.GREATERTHAN){
                return left > right?1:0;
            }else if (((BinaryOperatorNode)root).getToken().getType() == TokenType.GREATERTHANOREQUALSTO){
                return left >= right?1:0;
            }
        }

        if (root instanceof VarAccessNode){
            String name = (((VarAccessNode) root).getToken().getValue());
            String value = symbolTable.get(name);
           //symbolTable.printAll();

            if(value == null){
                throw new InvalidSyntaxException("variable "+name+" not defined");
            }

            return Integer.parseInt(value);
        }

        if (root instanceof VarAssignNode){
            String name = (((VarAssignNode) root).getToken().getValue());
            String value =(((VarAssignNode) root).getValueNode());
            System.out.println("here1");
            

            symbolTable.set(name, value);
            symbolTable.printAll();

            return Integer.parseInt(value);
        }

        if (root instanceof ListNode){
            List<Integer> elements = new ArrayList<>();

            List<Node> nodeElements = ((ListNode)root).getElementNodes();

            for ( Node elementNode : nodeElements){
                elements.add(evaluateExpression(elementNode));
            }

            if(elements.size() == 1){
                System.out.println(elements.get(0));
            }else{
                System.out.println(elements);
            }
            return 1;
        }

        if(root instanceof IfNode){
            List<List<Node>> cases = (((IfNode)root).getCases());
            Node elseNode = ((IfNode)root).getElseCase();

            for (List<Node> list: cases){
                int value = evaluateExpression(list.get(0));

                if(value != 0){
                    return evaluateExpression(list.get(1));
                }
            }

            if (elseNode != null){
                return evaluateExpression(elseNode);
            }

        }

        if(root instanceof WhileNode){
            List<Integer> elements = new ArrayList<>();

            while (true){
                int condition = evaluateExpression(((WhileNode)root).getCondition());

                if(condition == 0)
                    break;

                elements.add(evaluateExpression(((WhileNode)root).getBody()));
            }
            return 1;
        }

        if (root instanceof ForNode){
            List<Integer> elements = new ArrayList<>();

            int startValue = evaluateExpression(((ForNode)root).getStartValue());
            int endValue = evaluateExpression(((ForNode)root).getEndValue());

            while(startValue < endValue){
                symbolTable.set(((ForNode)root).getVarName().getValue(),String.valueOf(startValue));
                startValue += 1;

                elements.add(evaluateExpression(((ForNode)root).getBody()));
            }
            System.out.println(elements);
            return 1;
        }

        if (root instanceof FuncDefNode){
            String funcName = ((FuncDefNode)root).getVarNameToken().getValue();
            functionSymbolTable.set(funcName, ((FuncDefNode)root));
            return 1;
        }

        if (root instanceof CallNode){
            String functionName = ((CallNode) root).getNodeToCall().getToken().getValue();
            FuncDefNode funcDefNode = (FuncDefNode) functionSymbolTable.get(functionName);


            List<Node> argNodes = ((CallNode)root).argNodes();
            List<Token> argNames = funcDefNode.getArgNameToken();

            if ( argNames.size() > argNodes.size()){
                throw new InvalidSyntaxException("Less than required arguments passed");
            } else if ( argNames.size() < argNodes.size()){
                throw new InvalidSyntaxException("More than required arguments passed");
            }   


            int count = 0;
            for (Token argName: argNames){
                int argValue = evaluateExpression(argNodes.get(count));
                symbolTable.set(argName.getValue(), String.valueOf(argValue));
                count++;
            }

            Node body = funcDefNode.getBodyNode();
            
            return evaluateExpression(body);
            
        }

        throw new InvalidSyntaxException("Unknown Operator");
    }

}
