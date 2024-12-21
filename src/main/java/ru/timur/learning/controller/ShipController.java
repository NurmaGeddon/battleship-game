package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.timur.learning.configuration.security.details.CustomUserDetails;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.model.dto.ShipDto;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShipService;

@RestController
@RequestMapping("/game/{gameId}/ship")
@RequiredArgsConstructor
public class ShipController {
    private final ShipService shipService;

    private final GameService gameService;

    @PostMapping
    public GameDto placeShip(@AuthenticationPrincipal final CustomUserDetails userDetails,
                             @PathVariable final Long gameId,
                             @RequestBody final PGpoint[] coordinates) {
        Game game = gameService.getGame(gameId);
        shipService.placeShip(game, userDetails.getUserId(), coordinates);
        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }

    @PutMapping("{shipId}")
    public GameDto changeShipPlacement(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                       @PathVariable final Long gameId,
                                       @PathVariable final Long shipId,
                                       @RequestBody final PGpoint[] coordinates) {
        Game game = gameService.getGame(gameId);
        shipService.changeShipPlacement(game, userDetails.getUserId(), shipId, coordinates);
        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }

    @DeleteMapping("{shipId}")
    public GameDto deleteShip(@AuthenticationPrincipal final CustomUserDetails userDetails,
                              @PathVariable final Long shipId,
                              @PathVariable final Long gameId) {
        shipService.deleteShip(shipId);
        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }
}
