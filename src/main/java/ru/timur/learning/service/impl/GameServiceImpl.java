package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.InitiallyPlacedShips;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.service.GameEntityService;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShotService;
import ru.timur.learning.service.ShipService;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameEntityService gameEntityService;

    private final ShipService shipService;

    private final ShotService shotService;

    private final Queue<Long> gamesToJoin = new ArrayDeque<>();

    @Override
    public Long createGame(Long userId) {
        Long newGameId = gameEntityService.createGameEntity(userId);
        gamesToJoin.add(newGameId);

        return newGameId;
    }

    @Override
    public Long joinGame(Long userId) {
        checkGotGamesWaitingForPlayer2();
        Long idGameToJoin = gamesToJoin.poll();

        return gameEntityService.joinGame(idGameToJoin, userId);
    }

    private void checkGotGamesWaitingForPlayer2() {
        if (gamesToJoin.peek() == null) {
            throw new RuntimeException("Game to join not found. You should create game");
        }
    }

    @Override
    public GameDto getGameDtoForUser(Long gameId, Long userId) {
        Game game = getGame(gameId);
        return new GameDto(game, userId);
    }

    @Override
    public void changeUserStatusToReady(Long gameId, Long userId) {
        Game game = getGame(gameId);
        game.checkPlayerFinishedShipPlacement(userId);

        gameEntityService.updateUserStatusToReady(gameId, userId);
        gameEntityService.tryToStartGame(gameId);
    }

    @Override
    public void checkIfPlayerWon(Long gameId, Long userId) {
        Game game = getGame(gameId);
        Boolean playerWon = game.checkPlayerWon(userId);

        if (playerWon) {
            gameEntityService.changeGameEntityForWinner(gameId, userId);
            throw new RuntimeException("Player №" + game.getPlayerNumberForGame(userId) + " is won");
        }
    }

    @Override
    public Game getGame(Long gameId) {
        GameEntity gameEntity = gameEntityService.findById(gameId);
        Board player1Board = createBoard(gameId, 1);
        Board player2Board = createBoard(gameId, 2);
        return new Game(gameEntity, player1Board, player2Board);
    }

    private Board createBoard(Long gameId, Integer playerNumber) {
        InitiallyPlacedShips initiallyPlacedShips = shipService.createInitiallyPlacedShips(gameId, playerNumber);
        List<ShipEntity> shipsForPlayer = shipService.getShipsForPlayer(gameId, playerNumber);

        final Integer opponentPlayerNumber = getOpponentPlayerNumber(playerNumber);
        List<PGpoint> shotsCoordinates = shotService.getCoordinates(gameId, opponentPlayerNumber);

        return new Board(initiallyPlacedShips, shipsForPlayer, shotsCoordinates);
    }

    private Integer getOpponentPlayerNumber(final Integer playerNumber) {
        return playerNumber.equals(1) ? 2 : 1;
    }
}
