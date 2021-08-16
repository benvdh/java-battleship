package battleship.exceptions;

public class InvalidCoordinateOnGridException extends RuntimeException {
    public InvalidCoordinateOnGridException(String message) {
        super(message);
    }
}
