public class RunTimeException extends RuntimeException{
    public RunTimeException() {
        super("Invalid Syntax");
      }
    
      public RunTimeException(String message) {
        super(message);
      }
    
      public RunTimeException(String message, Throwable cause) {
        super(message, cause);
      }
}
