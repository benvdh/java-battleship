package battleship.ships;

import battleship.exceptions.CoordinateAlreadyHitException;
import battleship.exceptions.ShipNotLocatedHereException;
import battleship.grids.GridCoordinate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShipBase {
    private final int length;
    private final String name;
    private ShipState shipState;
    private List<GridCoordinate> occupiedCoordinates;
    private final Set<GridCoordinate> hitCoordinates;

    public ShipBase(int length, String name) {
        this.length = length;
        this.name = name;
        this.shipState = ShipState.INTACT;
        this.hitCoordinates = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public ShipState getShipState() {
        return shipState;
    }

    public void hitThisShipAtCoordinate(GridCoordinate coordinate) {
        if (hitCoordinates.contains(coordinate)) {
            throw new CoordinateAlreadyHitException("You've already hit this ship at this location! Try again:");
        }

        if (!isShipLocatedAtCoordinate(coordinate)) {
            throw new ShipNotLocatedHereException("This ship is not located at the provided coordinate!");
        } else {
            if (hitCoordinates.isEmpty()) {
                shipState = ShipState.PARTIALLY_HIT;
            }

            hitCoordinates.add(coordinate);

            if (hitCoordinates.containsAll(occupiedCoordinates)) {
                shipState = ShipState.DESTROYED;
            }
        }
    }

    public void setOccupiedCoordinates(List<GridCoordinate> occupiedCoordinates) {
        this.occupiedCoordinates = occupiedCoordinates;
    }

    public boolean isShipLocatedAtCoordinate(GridCoordinate coordinate) {
        return occupiedCoordinates.contains(coordinate);
    }
}
