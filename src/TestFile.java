import java.util.List;
import java.util.Scanner;

public class TestFile {

    public static void main(String args[]) {
      SymbolTable globalSymbolTable = new SymbolTable();
      globalSymbolTable.set("NULL", "0");
      globalSymbolTable.set("TRUE", "1");
      globalSymbolTable.set("FALSE", "0");
      globalSymbolTable.set("result", "1");
      globalSymbolTable.set("i", "1");
      globalSymbolTable.set("x","1");

      FunctionSymbolTable functionSymbolTable = new FunctionSymbolTable();

      String text = FileReader.getResourceFileAsString("code.x");
      
      if(text != null){
        runProgram(text, globalSymbolTable, functionSymbolTable);
      }else{
        while(true){
        
          System.out.print("nikc> ");
          Scanner sc = new Scanner(System.in);
          text = sc.nextLine();
          
          runProgram(text, globalSymbolTable, functionSymbolTable);
        }
      }

    }

    public static void runProgram(String text, SymbolTable globalSymbolTable, FunctionSymbolTable functionSymbolTable){
        // if (text == ""){
        //   continue;
        // }

        Lexer lexer = new Lexer(text);
        List<Token> tokens = lexer.makeTokens();

        // for (Token token: tokens){
        //   token.print();
        // }
        
        Parser parser = new Parser(tokens, globalSymbolTable, functionSymbolTable);
        SyntaxTree expression = parser.parse();

        Evaluator evaluator = new Evaluator(expression.getNode(), globalSymbolTable, functionSymbolTable);
        System.out.println(evaluator.evaluate());
      
    }
  }
   