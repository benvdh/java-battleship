package battleship.exceptions;

public class CoordinatesDoNotMatchShipLengthException extends RuntimeException{
    public CoordinatesDoNotMatchShipLengthException(String message) {
        super(message);
    }
}
