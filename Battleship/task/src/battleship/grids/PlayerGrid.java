package battleship.grids;

import battleship.exceptions.CoordinatesDoNotMatchShipLengthException;
import battleship.exceptions.ShipsTooCloseException;
import battleship.ships.ShipBase;

import java.util.List;

public class PlayerGrid extends GridBase {

    public PlayerGrid() {
        super(FieldState.getAllowedOwnFieldStates());
    }

    public boolean placeShip(ShipBase ship, GridCoordinate start, GridCoordinate end) {
        if (!(ship.getLength() == start.distanceTo(end))) {
            throw new CoordinatesDoNotMatchShipLengthException(
                String.format("Error! Wrong length of the %s! Try again:%n", ship.getName())
            );
        }

        List<GridCoordinate> boundingBox = calculateBoundingBoxForShip(start, end);

        if (doesAreaHaveOccupiedFields(boundingBox)) {
            throw new ShipsTooCloseException("Error! You placed it too close to another one. Try again:\n");
        }

        List<GridCoordinate> shipCoordinates = setFieldStateForArea(FieldState.OCCUPIED_BY_SHIP, start, end);
        ship.setOccupiedCoordinates(shipCoordinates);

        return true;
    }



    /**
     * Calculates the smallest bounding box around the ship. The bounding box includes grid
     * edges, when the bounding box falls partly outside the grid.
     *
     * @param start The start coordinate of where the ship should be positioned
     * @param end The end coordinate of where the ship should be positioned
     * @return list of 2 coordinates with the upper left (index 0), and lower right corner (index 1) of the
     * bounding box, respectively.
     */
    private List<GridCoordinate> calculateBoundingBoxForShip(
        GridCoordinate start,
        GridCoordinate end
    ) {
        List<GridCoordinate> sortedCoordinates = sortCoordinates(start, end);

        GridCoordinate lowest = sortedCoordinates.get(0);
        GridCoordinate highest = sortedCoordinates.get(1);

        int highestColumn = fieldStateGrid.firstEntry().getValue().size();

        char upperLeftRow = lowest.getRow() == fieldStateGrid.firstKey() ?
            fieldStateGrid.firstKey() : (char) ((int) lowest.getRow() - 1);
        int upperLeftColumn = lowest.getColumn() == 1 ?
            1 : lowest.getColumn() - 1;
        GridCoordinate upperLeftCorner = new GridCoordinate(upperLeftRow, upperLeftColumn);

        char lowerRightRow = highest.getRow() == fieldStateGrid.lastKey() ?
            fieldStateGrid.lastKey() : (char) ((int) highest.getRow() + 1);
        int lowerRightColumn = highest.getColumn() == highestColumn ?
            highestColumn : highest.getColumn() + 1;
        GridCoordinate lowerRightCorner = new GridCoordinate(lowerRightRow, lowerRightColumn);

        return List.of(upperLeftCorner, lowerRightCorner);
    }
}
