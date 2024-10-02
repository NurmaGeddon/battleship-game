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
    public GameDto placeShip(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long gameId,
                             @RequestBody PGpoint[] coordinates) {
        ShipDto shipDto = new ShipDto(coordinates);
        Game game = gameService.getGame(gameId);
        shipService.placeShip(game, userDetails.getUserId(), shipDto);
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }

    @PutMapping("{shipId}")
    public GameDto changeShipPlacement(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable Long gameId,
                                       @PathVariable Long shipId,
                                       @RequestBody PGpoint[] coordinates) {
        ShipDto shipDto = new ShipDto(coordinates);
        shipService.changeShipPlacement(gameId, shipId, shipDto);
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }

    @DeleteMapping("{shipId}")
    public GameDto deleteShip(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long shipId,
                              @PathVariable Long gameId) {
        shipService.deleteShip(shipId);
        return gameService.getGameForUser(gameId, userDetails.getUserId());
    }
}
