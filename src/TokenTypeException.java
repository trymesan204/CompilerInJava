public class TokenTypeException extends RuntimeException {
    public TokenTypeException() {
        super("Invalid Token");
      }
    
      public TokenTypeException(String message) {
        super(message);
      }
    
      public TokenTypeException(String message, Throwable cause) {
        super(message, cause);
      }
}
