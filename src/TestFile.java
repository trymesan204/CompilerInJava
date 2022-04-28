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
      globalSymbolTable.set("i", "1");
      globalSymbolTable.set("x","1");

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
        
        Parser parser = new Parser(tokens, globalSymbolTable, functionSymbolTable, tokensAssem);
        SyntaxTree expression = parser.parse();

        Assembly assembly = new Assembly(tokensAssem);
        assembly.createAssembly();

        //Evaluator evaluator = new Evaluator(expression.getNode(), globalSymbolTable, functionSymbolTable);
        //System.out.println(evaluator.evaluate());
      
    }
  }
   