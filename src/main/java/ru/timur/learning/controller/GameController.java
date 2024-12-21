package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.configuration.security.details.CustomUserDetails;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.service.GameService;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    /**
     * Creates new game
     * @param userDetails
     * @return created game ID
     */
    @PostMapping("/create")
    public Long createGame(@AuthenticationPrincipal final CustomUserDetails userDetails) {
        return gameService.createGame(userDetails.getUserId());
    }

    /**
     * Joins user to game without a second player
     * @param userDetails
     * @return created game ID
     */
    @PostMapping("/auto_join")
    public Long joinGame(@AuthenticationPrincipal final CustomUserDetails userDetails) {
        return gameService.joinGame(userDetails.getUserId());
    }

    /**
     * Returns game view depending on player
     * @param userDetails
     * @param gameId id of the game
     * @return dto object for game
     */
    @GetMapping("/{gameId}")
    public GameDto getGame(@AuthenticationPrincipal final CustomUserDetails userDetails,
                           @PathVariable final Long gameId) {
        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }

    /**
     * Signals to server that player is finished placing ships
     * @param userDetails
     * @param gameId id of the game
     * @return dto object for game
     */
    @PostMapping("{gameId}/ready")
    public GameDto playerReadyForGame(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                      @PathVariable final Long gameId) {
        gameService.changeUserStatusToReady(gameId, userDetails.getUserId());
        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }
}
