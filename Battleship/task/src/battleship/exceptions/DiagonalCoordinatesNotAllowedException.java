package battleship.exceptions;

public class DiagonalCoordinatesNotAllowedException extends RuntimeException {
    public DiagonalCoordinatesNotAllowedException(String message) {
        super(message);
    }
}
