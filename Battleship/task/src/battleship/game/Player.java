package battleship.game;

import battleship.grids.OpponentGrid;
import battleship.grids.PlayerGrid;
import battleship.ships.PlayerFleet;

import java.util.Objects;

public class Player {
    private final String name;
    private final PlayerGrid playerGrid;
    private final OpponentGrid opponentGrid;
    private final PlayerFleet playerFleet;

    public Player(String name) {
        this.name = name;
        this.playerGrid = new PlayerGrid();
        this.opponentGrid = new OpponentGrid();
        this.playerFleet = new PlayerFleet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && playerGrid.equals(player.playerGrid) && opponentGrid.equals(player.opponentGrid) && playerFleet.equals(player.playerFleet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, playerGrid, opponentGrid, playerFleet);
    }

    public String getName() {
        return name;
    }

    public PlayerGrid getPlayerGrid() {
        return playerGrid;
    }

    public OpponentGrid getOpponentGrid() {
        return opponentGrid;
    }

    public PlayerFleet getPlayerFleet() {
        return playerFleet;
    }

}
