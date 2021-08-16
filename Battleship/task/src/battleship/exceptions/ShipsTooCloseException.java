package battleship.exceptions;

public class ShipsTooCloseException extends RuntimeException {
    public ShipsTooCloseException(String message) {
        super(message);
    }
}
