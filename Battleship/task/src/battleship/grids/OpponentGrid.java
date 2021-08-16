package battleship.grids;

public class OpponentGrid extends GridBase {

    public OpponentGrid() {
        super(FieldState.getAllowedOpponentFieldStates());
    }
}
