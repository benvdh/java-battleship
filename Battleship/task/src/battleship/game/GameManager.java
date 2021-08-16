package battleship.game;

import battleship.exceptions.CoordinateAlreadyHitException;
import battleship.grids.FieldState;
import battleship.grids.GridCoordinate;
import battleship.ships.ShipBase;
import battleship.ships.ShipState;

import java.util.List;

public class GameManager {
    private final List<Player> players;
    private Player currentPlayer;


    public GameManager(Player... players) {
        this.players = List.of(players);
        currentPlayer = this.players.get(0);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player switchPlayer() {
        this.currentPlayer = getNextPlayer();

        return currentPlayer;
    }

    private Player getNextPlayer() {
        // switch players in a round-robin fashion
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = currentPlayerIndex != players.size() - 1 ? currentPlayerIndex + 1 : 0;

        return players.get(nextPlayerIndex);
    }

    public String fireShot(GridCoordinate coordinate) {
        Player nextPlayer = getNextPlayer();

        FieldState currentFieldState = nextPlayer.getPlayerGrid().getFieldStateByCoordinate(coordinate);

        if (currentFieldState == FieldState.OCCUPIED_BY_SHIP) {
            nextPlayer.getPlayerGrid().setFieldStateForCoordinate(FieldState.SHIP_HIT, coordinate);
            currentPlayer.getOpponentGrid().setFieldStateForCoordinate(FieldState.SHIP_HIT, coordinate);
            ShipBase ship = nextPlayer.getPlayerFleet().getShipByCoordinate(coordinate);

            if (ship != null) {
                ship.hitThisShipAtCoordinate(coordinate);

                return ship.getShipState() == ShipState.DESTROYED ?
                    "You sank a ship!" :
                    "You hit a ship!";
            }
        }

        if (currentFieldState == FieldState.FOG_OF_WAR) {
            nextPlayer.getPlayerGrid().setFieldStateForCoordinate(FieldState.SHOT_MISSED, coordinate);
            currentPlayer.getOpponentGrid().setFieldStateForCoordinate(FieldState.SHOT_MISSED, coordinate);
            return "You missed!";
        } else {
            throw new CoordinateAlreadyHitException("Why are you trying to shoot a position you've already " +
                "shot at? Please keep a proper record of fired shots!");
        }
    }

    public boolean hasAnyFleetBeenDestroyed() {
        return players.stream().anyMatch(x -> x.getPlayerFleet().hasBeenDestroyed());
    }


}
