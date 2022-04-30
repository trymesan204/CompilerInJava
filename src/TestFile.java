import java.util.ArrayList;
import java.util.List;  
import java.util.Scanner;

public class TestFile {

    public static void main(String args[]) {
      SymbolTable globalSymbolTable = new SymbolTable();
      globalSymbolTable.set("NULL", "0");
      globalSymbolTable.set("TRUE", "1");
      globalSymbolTable.set("FALSE", "0");
      globalSymbolTable.set("result", "1");

      FunctionSymbolTable functionSymbolTable = new FunctionSymbolTable();

      List<Token> tokens = new ArrayList<>();

      String text = FileReader.getResourceFileAsString("code.x");
      
      if(text != null){
        runProgram(text, globalSymbolTable, functionSymbolTable, tokens);
      }else{
        while(true){
        
          System.out.print("nikc> ");
          Scanner sc = new Scanner(System.in);
          text = sc.nextLine();
          
          runProgram(text, globalSymbolTable, functionSymbolTable, tokens);
        }
      }

    }

    public static void runProgram(String text, SymbolTable globalSymbolTable, FunctionSymbolTable functionSymbolTable, List<Token> tokensAssem){

        Lexer lexer = new Lexer(text);
        List<Token> tokens = lexer.makeTokens();
        
        Parser parser = new Parser(tokens, tokensAssem);
        SyntaxTree expression = parser.parse();

        System.out.println("--------------------");
        System.out.println("Assembly Code");
        Assembly assembly = new Assembly(tokensAssem);
        assembly.createAssembly();


        System.out.println("--------------------");
        System.out.println("Output");
        Evaluator evaluator = new Evaluator(expression.getNode(), globalSymbolTable, functionSymbolTable);
        evaluator.evaluate();
      
    }
  }
   