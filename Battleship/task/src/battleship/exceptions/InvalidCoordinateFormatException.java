package battleship.exceptions;

public class InvalidCoordinateFormatException extends RuntimeException {
    public InvalidCoordinateFormatException(String message) {
        super(message);
    }
}
