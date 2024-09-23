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

    @PostMapping("/create")
    public String createGame(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long gameId = gameService.createGame(userDetails.getUserId());
        return gameId.toString();
    }

    @PostMapping("/auto_join")
    public String joinGame(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long gameId = gameService.joinGame(userDetails.getUserId());
        return gameId.toString();
    }

    @GetMapping("/{gameId}")
    public GameDto getGamePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long gameId) {
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }

    @PostMapping("{gameId}/ready")
    public GameDto playerReadyForGame(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long gameId) {
        gameService.userReadyForGame(gameId, userDetails.getUserId());
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }
}
