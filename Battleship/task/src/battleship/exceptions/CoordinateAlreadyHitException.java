package battleship.exceptions;

public class CoordinateAlreadyHitException extends RuntimeException {
    public CoordinateAlreadyHitException(String message) {
        super(message);
    }
}
