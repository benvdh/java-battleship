package battleship.exceptions;

public class ShipNotLocatedHereException extends RuntimeException {
    public ShipNotLocatedHereException(String message) {
        super(message);
    }
}
