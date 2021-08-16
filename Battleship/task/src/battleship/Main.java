package battleship;


import battleship.exceptions.*;
import battleship.game.GameManager;
import battleship.game.Player;
import battleship.grids.GridBase;
import battleship.grids.GridCoordinate;
import battleship.ships.ShipBase;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        GameManager gameManager = new GameManager(player1, player2);

        for (Player player : gameManager.getPlayers()) {
            showAllocateShipsPrompt(player, scanner);
            showSwitchPlayerPrompt(scanner);
            gameManager.switchPlayer();
        }

        startGame(gameManager, scanner);
    }

    public static void showGrid(GridBase grid) {
        String formattedGrid = grid.getFormattedGrid();

        System.out.println(formattedGrid);
    }

    public static void showDoubleGrid(Player player) {
        String separator = "---------------------";

        System.out.println(player.getOpponentGrid().getFormattedGrid().stripTrailing());
        System.out.println(separator);
        showGrid(player.getPlayerGrid());
    }

    public static void showAllocateShipsPrompt(Player player, Scanner scanner) {
        System.out.printf("%s, place your ships on the game field%n", player.getName());

        System.out.println();

        showGrid(player.getPlayerGrid());

        for (ShipBase ship : player.getPlayerFleet().getShips()) {

            System.out.printf("Enter the coordinates of the %s (%d cells):%n%n", ship.getName(), ship.getLength());
            boolean shipPlaced = false;

            while (!shipPlaced) {
                try {
                    String desiredCoordinates = scanner.nextLine();

                    List<GridCoordinate> parsedCoordinates =
                        Arrays.stream(desiredCoordinates.split(" "))
                            .map(GridCoordinate::new)
                            .collect(Collectors.toList());

                    shipPlaced = player.getPlayerGrid().placeShip(
                        ship,
                        parsedCoordinates.get(0),
                        parsedCoordinates.get(1)
                    );

                } catch (CoordinatesDoNotMatchShipLengthException | ShipsTooCloseException e) {
                    System.out.println();
                    System.out.println(e.getMessage());
                } catch (DiagonalCoordinatesNotAllowedException e) {
                    System.out.println();
                    System.out.println("Error! Wrong ship location! Try again:\n");
                }
            }

            System.out.println();
            showGrid(player.getPlayerGrid());
        }
    }

    public static void showSwitchPlayerPrompt(Scanner scanner) {
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }

    public static void startGame(
        GameManager gameManager,
        Scanner scanner
    ) {
        Player currentPlayer = gameManager.getCurrentPlayer();

        while (!gameManager.hasAnyFleetBeenDestroyed()) {
            try {
                showDoubleGrid(currentPlayer);

                System.out.printf("%s, it's your turn:\n", currentPlayer.getName());
                String coordinateString = scanner.nextLine();
                GridCoordinate coordinateToShoot = new GridCoordinate(coordinateString);

                String message = gameManager.fireShot(coordinateToShoot);

                System.out.println();

                if (!gameManager.hasAnyFleetBeenDestroyed()) {
                    System.out.println(message);
                }

            } catch (InvalidCoordinateOnGridException | ShipNotLocatedHereException e) {
                System.out.println();
                System.out.println(e.getMessage());
            } catch (CoordinateAlreadyHitException e) {
                System.out.println();

                if (!gameManager.hasAnyFleetBeenDestroyed()) {
                    System.out.println(e.getMessage());
                }
            }

            if (!gameManager.hasAnyFleetBeenDestroyed()) {
                showSwitchPlayerPrompt(scanner);
                currentPlayer = gameManager.switchPlayer();
            }
        }

        System.out.println("You sank the last ship. You won. Congratulations!");
    }
}
