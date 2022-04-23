public class InvalidSyntaxException extends RuntimeException {
    public InvalidSyntaxException() {
        super("Invalid Syntax");
      }
    
      public InvalidSyntaxException(String message) {
        super(message);
      }
    
      public InvalidSyntaxException(String message, Throwable cause) {
        super(message, cause);
      }
}
