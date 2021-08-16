package battleship.grids;

import battleship.exceptions.DiagonalCoordinatesNotAllowedException;
import battleship.exceptions.InvalidCoordinateFormatException;

import java.util.Objects;

public class GridCoordinate implements Comparable<GridCoordinate> {
    private final String coordinate;
    private final char row;
    private final int column;
    private final int columnIndex;

    public GridCoordinate(String coordinate) {
        if (!hasValidCoordinateFormat(coordinate)) {
            throw new InvalidCoordinateFormatException("Error! Expected a capital letter " +
                "followed by a number larger than 0. Got: " + coordinate);
        }

        this.coordinate = coordinate;
        this.row = extractRowCharacter();
        this.column = extractColumnNumber();
        this.columnIndex = column - 1;
    }

    public GridCoordinate(char row, int column) {
        this(String.valueOf(row).concat(String.valueOf(column)));
    }

    private Character extractRowCharacter() {
        return coordinate.charAt(0);
    }

    private int extractColumnNumber() {
        return Integer.parseInt(
            coordinate.substring(1)
        );
    }

    @Override
    public String toString() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridCoordinate that = (GridCoordinate) o;
        return coordinate.equals(that.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }

    public static boolean hasValidCoordinateFormat(String coordinate) {
        return coordinate.matches("^[A-Z][1-9][0-9]*$");
    }

    public static boolean areDiagonalCoordinates(GridCoordinate c1, GridCoordinate c2) {
        return !c1.equals(c2) && c1.row != c2.row && c1.column != c2.column;
    }

    public int distanceTo(GridCoordinate coordinate) {
        if (areDiagonalCoordinates(this, coordinate)) {
            throw new DiagonalCoordinatesNotAllowedException(String.format("Error! %s and %s are on a diagonal. Only" +
                " distances between coordinates on a vertical or horizontal line will be calculated!", this, coordinate)
            );
        }

        // we add one as both boundaries should be included (e.g. 7-3 = 4, but a range of 3 up to and including 7
        // occupies 5 cells)
        int distance = 1;
        int diff;

        if (this.row == coordinate.row) {
            diff = this.column > coordinate.column ? this.column - coordinate.column : coordinate.column - this.column;
        } else {
            int thisCoordId = this.row;
            int otherCoordId = coordinate.row;

            diff = thisCoordId > otherCoordId ? thisCoordId - otherCoordId : otherCoordId - thisCoordId;
        }

        return distance + diff;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getColumn() {
        return column;
    }

    public char getRow() {
        return row;
    }

    /**
     * Compares two GridCoordinates.
     * The coordinate that is closest to the right lower corner, and
     * has the highest row and column index, is considered greater than
     * coordinates, closer to the left upper corner.
     *
     * @param coordinate The coordinate to compare this coordinate to
     * @return -1 if this object is smaller than the provided coordinate,
     * 0 if the two coordinates are equal, and 1 if this coordinate is larger
     * than the provided coordinate.
     */
    @Override
    public int compareTo(GridCoordinate coordinate) {
        if (this.equals(coordinate)) {
            return 0;
        }

        if (this.row > coordinate.row) {
            return 1;
        }

        if (this.row == coordinate.row && this.column > coordinate.column) {
            return 1;
        }

        return -1;

    }
}
