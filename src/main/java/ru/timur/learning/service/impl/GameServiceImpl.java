package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.ShipsOnBoard;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShotService;
import ru.timur.learning.service.ShipService;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final ShipService shipService;

    private final ShotService shotService;

    private final Queue<Long> gamesToJoin = new ArrayDeque<>();

    private static GameEntity createNewGameEntity(Long userId) {
        return new GameEntity(
                null,
                userId,
                null,
                false,
                false,
                GameEntity.GameState.WAITING_FOR_PLAYER,
                null);
    }

    @Override
    public Long createGame(Long userId) {
        GameEntity game = createNewGameEntity(userId);

        GameEntity savedGame = gameRepository.save(game);
        gamesToJoin.add(savedGame.getId());

        return savedGame.getId();
    }

    @Override
    public Long joinGame(Long userId) {
        GameEntity gameEntity = getGameToJoin();
        checkUserNotPlayer1(gameEntity, userId);

        gameEntity.updateWithPlayer2(userId);
        return gameRepository.update(gameEntity).getId();
    }

    private void checkUserNotPlayer1(GameEntity gameEntity, Long userId) {
        if (gameEntity.getPlayer1Id().equals(userId)) {
            throw new IllegalArgumentException("Player trying to join game that he created");
        }
    }

    private GameEntity getGameToJoin() {
        checkGotGamesWaitingForPlayer2();
        Long idGameToJoin = gamesToJoin.poll();

        GameEntity gameToJoin = gameRepository.findById(idGameToJoin);
        checkGameWaitsForPlayer2(gameToJoin);
        return gameToJoin;
    }

    private void checkGameWaitsForPlayer2(GameEntity gameToJoin) {
        if (!gameToJoin.getGameState()
                .equals(GameEntity.GameState.WAITING_FOR_PLAYER) ||
            gameToJoin.getPlayer2Id() != null) {
            throw new IllegalArgumentException("Game already taken");
        }
    }

    private void checkGotGamesWaitingForPlayer2() {
        if (gamesToJoin.peek() == null) {
            throw new RuntimeException("Game to join not found. You should create game");
        }
    }

    @Override
    public Game getGame(Long gameId) {
        return constructGame(gameId);
    }

    @Override
    public GameDto getGameForUser(Long gameId, Long userId) {
        Game game = getGame(gameId);
        return new GameDto(game, userId);
    }

    @Override
    public void changeUserStatusToReady(Long gameId, Long userId) {
        checkPlayerFinishedShipPlacement(gameId, userId);

        GameEntity gameEntity = gameRepository.findById(gameId);
        updateGameEntity(gameEntity, userId);

        autoChangeGameState(gameId);
    }

    @Override
    public void changePlayerShotTurn(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        gameEntity.changePlayerShotTurn();
        gameRepository.update(gameEntity);
    }

    @Override
    public void checkIfPlayerWon(Long gameId, Long userId) {
        Game game = getGame(gameId);
        Board board = game.getOpponentBoard(userId);

        if (board.checkPlayerWon()) {
            changeGameEntityForWinner(gameId, userId);
            throw new RuntimeException("Player №" + game.getPlayerNumberForGame(userId) + " is won");
        }
    }

    private void changeGameEntityForWinner(Long gameId, Long userId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        gameEntity.setWinnerId(userId);
        gameEntity.setGameState(GameEntity.GameState.GAME_FINISHED);
        gameRepository.update(gameEntity);
    }

    private void checkPlayerFinishedShipPlacement(Long gameId, Long userId) {
        Game game = getGame(gameId);
        ShipsOnBoard shipsOnBoard = game.getMyBoard(userId).getShipsOnBoard();
        shipsOnBoard.checkPlacedAllShips();
    }

    private void updateGameEntity(GameEntity gameEntity, Long userId) {
        gameEntity.changePlayerStatusToReady(userId);
        gameRepository.update(gameEntity);
    }

    private void autoChangeGameState(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);

        if (gameEntity.getPlayer1Ready() && gameEntity.getPlayer2Ready()) {
            gameEntity.setGameState(GameEntity.GameState.PLAYER1_TURN);
            gameRepository.update(gameEntity);
        }
    }

    private Game constructGame(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        Board player1Board = createBoard(gameId, 1);
        Board player2Board = createBoard(gameId, 2);
        return new Game(gameEntity, player1Board, player2Board);
    }

    private Board createBoard(Long gameId, Integer playerNumber) {
        ShipsOnBoard shipsOnBoard = shipService.createShipsOnBoard(gameId, playerNumber);
        List<ShipEntity> shipsForPlayer = shipService.getShipsForPlayer(gameId, playerNumber);

        Integer opponentPlayerNumber = playerNumber.equals(1) ? 2 : 1;
        List<PGpoint> shotsCoordinates = shotService.getCoordinates(gameId, opponentPlayerNumber);

        return new Board(shipsOnBoard, shipsForPlayer, shotsCoordinates);
    }
}
