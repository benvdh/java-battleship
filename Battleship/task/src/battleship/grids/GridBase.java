package battleship.grids;

import battleship.exceptions.ForbiddenFieldStateException;
import battleship.exceptions.InvalidCoordinateOnGridException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridBase {
    private static final int GRID_SIZE = 10;
    private static final String INVALID_COORDINATE_ERROR = "Error! You entered the wrong coordinates! Try again:\n";
    protected TreeMap<Character, ArrayList<FieldState>> fieldStateGrid;
    protected EnumSet<FieldState> allowedFieldStates;

    public GridBase(EnumSet<FieldState> allowedFieldStates) {
        this.fieldStateGrid = initializeFieldStateGrid();
        this.allowedFieldStates = allowedFieldStates;
    }

    private TreeMap<Character, ArrayList<FieldState>> initializeFieldStateGrid() {
        TreeMap<Character, ArrayList<FieldState>> grid = new TreeMap<>();
        FieldState[] fogOfWarRow = new FieldState[GRID_SIZE];
        Arrays.fill(fogOfWarRow, FieldState.FOG_OF_WAR);
        ArrayList<FieldState> initialRow = new ArrayList<>(Arrays.asList(fogOfWarRow));

        char c = 'A';
        for (int i = 0; i < GRID_SIZE; i++) {
            grid.put(c, new ArrayList<>(initialRow));
            c++;
        }

        return grid;
    }

    private TreeMap<Character, List<Character>> getFieldStateSymbolGrid() {
        TreeMap<Character, List<Character>> symbolGrid = new TreeMap<>();

        for (Map.Entry<Character, ArrayList<FieldState>> row : fieldStateGrid.entrySet()) {
            symbolGrid.put(
                row.getKey(),
                row.getValue()
                    .stream()
                    .map(FieldState::getSymbol)
                    .collect(Collectors.toList())
            );
        }

        return symbolGrid;
    }

    public String getFormattedGrid() {
        TreeMap<Character, List<Character>> symbolGrid = getFieldStateSymbolGrid();
        StringBuilder builder = new StringBuilder();

        String headerRow = getHeaderRow();

        builder.append(headerRow).append("\n");

        for (Map.Entry<Character, List<Character>> row : symbolGrid.entrySet()) {
            builder.append(
                String.format(
                    "%s %s%n",
                    row.getKey(),
                    row.getValue()
                        .stream()
                        .map(Object::toString)
                        .collect(
                            Collectors.joining(" ")
                        )
                )
            );
        }

        return builder.toString();
    }

    private String getHeaderRow() {
        String headerRow =  IntStream
            .range(1, GRID_SIZE + 1)
            .mapToObj(String::valueOf)
            .collect(
                Collectors.joining(" ")
            );

        return "  ".concat(headerRow);
    }

    protected boolean doesAreaHaveOccupiedFields(List<GridCoordinate> areaBoundingBox) {
        int startColumn = areaBoundingBox.get(0).getColumnIndex();
        int endColumn = areaBoundingBox.get(1).getColumnIndex() + 1;

        char startRow = areaBoundingBox.get(0).getRow();
        char endRow = areaBoundingBox.get(1).getRow();

        for (char c = startRow; c <= endRow ; c++) {
            ArrayList<FieldState> row = fieldStateGrid.get(c);
            boolean hasShip = row.subList(startColumn, endColumn).contains(FieldState.OCCUPIED_BY_SHIP);

            if (hasShip) {
                return true;
            }
        }
        return false;
    }

    protected List<GridCoordinate> sortCoordinates(GridCoordinate first, GridCoordinate second) {
        ArrayList<GridCoordinate> coordinates = new ArrayList(List.of(first, second));
        Collections.sort(coordinates);

        return coordinates;
    }

    protected List<GridCoordinate> setFieldStateForArea(FieldState state, GridCoordinate start, GridCoordinate end) {
        if (!allowedFieldStates.contains(state)) {
            throw new ForbiddenFieldStateException(
                String.format("The FieldState %s is not allowed on this grid!", state.toString())
            );
        }

        List<GridCoordinate> sortedCoordinates = sortCoordinates(start, end);
        GridCoordinate lowest = sortedCoordinates.get(0);
        GridCoordinate highest = sortedCoordinates.get(1);
        List<GridCoordinate> changedFields = new ArrayList<>();

        for (char c = lowest.getRow(); c <= highest.getRow(); c++) {
            ArrayList<FieldState> row = fieldStateGrid.get(c);

            for (int i = lowest.getColumnIndex(); i <= highest.getColumnIndex() ; i++) {
                row.set(i, state);
                changedFields.add(new GridCoordinate(c, i + 1));
            }
        }

        return changedFields;
    }

    public FieldState getFieldStateByCoordinate(GridCoordinate coordinate) {
        try {
            return fieldStateGrid.get(coordinate.getRow()).get(coordinate.getColumnIndex());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidCoordinateOnGridException(INVALID_COORDINATE_ERROR);
        }
    }

    public void setFieldStateForCoordinate(FieldState state, GridCoordinate coordinate) {
        try {
            fieldStateGrid.get(coordinate.getRow()).set(coordinate.getColumnIndex(), state);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidCoordinateOnGridException(INVALID_COORDINATE_ERROR);
        }

    }
}
