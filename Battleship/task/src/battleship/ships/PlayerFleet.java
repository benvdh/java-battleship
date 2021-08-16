package battleship.ships;

import battleship.grids.GridCoordinate;

import java.util.List;

public class PlayerFleet {
    private List<ShipBase> ships;

    public PlayerFleet() {
        this.ships = List.of(
            new AircraftCarrier(),
            new BattleShip(),
            new Submarine(),
            new Cruiser(),
            new Destroyer()
        );
    }

    public List<ShipBase> getShips() {
        return ships;
    }

    public ShipBase getShipByCoordinate(GridCoordinate coordinate) {
        for (ShipBase ship : ships) {
            if (ship.isShipLocatedAtCoordinate(coordinate)) {
                return ship;
            }
        }

        return null;
    }

    public boolean hasBeenDestroyed() {
        return ships.stream().allMatch(x -> x.getShipState() == ShipState.DESTROYED);
    }
}
