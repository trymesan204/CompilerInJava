public class TokenTypeException extends RuntimeException {
    public TokenTypeException() {
        super("Invalid Token");
      }

      public TokenTypeException(char message) {
        super("Invalid token \'"+message+"\'");

      }
    
      public TokenTypeException(String message) {
        super(message);
      }
    
      public TokenTypeException(String message, Throwable cause) {
        super(message, cause);
      }
}
