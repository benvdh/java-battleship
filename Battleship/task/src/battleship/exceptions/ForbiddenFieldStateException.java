package battleship.exceptions;

public class ForbiddenFieldStateException extends RuntimeException{
    public ForbiddenFieldStateException(String message) {
        super(message);
    }
}
