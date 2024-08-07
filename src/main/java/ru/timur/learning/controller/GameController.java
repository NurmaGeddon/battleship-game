package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.configuration.security.details.CustomUserDetails;
import ru.timur.learning.model.Game;
import ru.timur.learning.service.GameService;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public String createGame(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long gameId = gameService.createGame(userDetails.getUserId());
        return "redirect:/game/" + gameId.toString();
    }

    @PostMapping("/join")
    public String joinGame(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long gameId = gameService.joinGame(userDetails.getUserId());
        return "redirect:/game/" + gameId;
    }

    @ResponseBody
    @GetMapping("/{gameId}")
    public Game getGamePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PathVariable Long gameId) {
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }

    @ResponseBody
    @PostMapping("/{gameId}/place_ships")
    public Game placeShips(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long gameId) {
        Object grid;
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }
}
