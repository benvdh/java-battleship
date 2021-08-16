package battleship.grids;

import java.util.EnumSet;

public enum FieldState {
    FOG_OF_WAR('~'),
    OCCUPIED_BY_SHIP('O'),
    SHIP_HIT('X'),
    SHOT_MISSED('M');

    char symbol;

    FieldState(char symbol) {
        this.symbol = symbol;
    }

    public static EnumSet<FieldState> getAllowedOwnFieldStates() {
        return EnumSet.of(FOG_OF_WAR, OCCUPIED_BY_SHIP, SHIP_HIT, SHOT_MISSED);
    }

    public char getSymbol() {
        return symbol;
    }

    public static EnumSet<FieldState> getAllowedOpponentFieldStates() {
        return EnumSet.of(FOG_OF_WAR, SHIP_HIT, SHOT_MISSED);
    }
}
