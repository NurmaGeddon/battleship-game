package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.timur.learning.configuration.security.details.CustomUserDetails;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.model.dto.ShotDto;
import ru.timur.learning.service.GameEntityService;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShotService;

@RestController
@RequiredArgsConstructor
public class ShotController {
    private final ShotService shotService;

    private final GameService gameService;

    private final GameEntityService gameEntityService;

    @PostMapping("/game/{gameId}/shot")
    public GameDto makeShot(@AuthenticationPrincipal final CustomUserDetails userDetails,
                            @PathVariable final Long gameId,
                            @RequestBody final PGpoint pGpoint) {
        ShotDto shotDto = new ShotDto(pGpoint);
        Game game = gameService.getGame(gameId);
        Long userId = userDetails.getUserId();

        shotService.takeShot(game, userId, shotDto);
        gameService.checkIfPlayerWon(gameId, userId);
        gameEntityService.changePlayerShotTurn(gameId);

        return gameService.getGameDtoForUser(gameId, userDetails.getUserId());
    }
}
