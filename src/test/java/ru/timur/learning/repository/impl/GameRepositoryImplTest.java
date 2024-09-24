package ru.timur.learning.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.configuration.TestConfig;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class GameRepositoryImplTest {

    private final GameRepository gameRepository;

    private GameEntity gameEntity;

    GameRepositoryImplTest(@Autowired GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        gameEntity = new GameEntity(
                null,
                1L,
                0L,
                false,
                false,
                null,
                0L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        GameEntity expected = gameEntity;
        GameEntity actual = gameRepository.save(expected);

        expected.setId(actual.getId());
        expected.setGameState(GameEntity.GameState.WAITING_FOR_PLAYER);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById() {
        GameEntity expected = gameRepository.save(gameEntity);

        GameEntity actual = gameRepository.findById(expected.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        GameEntity gameEntity1 = gameEntity;
        GameEntity gameEntity2 = new GameEntity(null, 2L, 0L, false, false, null, 0L);
        GameEntity gameEntity3 = new GameEntity(null, 3L, 0L, false, false, null, 0L);

        GameEntity saved1 = gameRepository.save(gameEntity1);
        GameEntity saved2 = gameRepository.save(gameEntity2);
        GameEntity saved3 = gameRepository.save(gameEntity3);

        List<GameEntity> expected = List.of(saved1,
                saved2,
                saved3);
        List<GameEntity> actual = gameRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
        GameEntity saved = gameRepository.save(gameEntity);

        GameEntity expected = new GameEntity(saved.getId(),
                2L,
                3L,
                true,
                true,
                GameEntity.GameState.GAME_FINISHED,
                3L);
        gameRepository.update(expected);
        GameEntity actual = gameRepository.findById(expected.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteById() {
        GameEntity saved = gameRepository.save(gameEntity);
        Assertions.assertTrue(gameRepository.deleteById(saved.getId()));
        Assertions.assertNull(gameRepository.findById(saved.getId()));
    }

    @Test
    void deleteAll() {
        GameEntity gameEntity1 = gameEntity;
        GameEntity gameEntity2 = new GameEntity(null, 2L, 0L, false, false, null, 0L);
        GameEntity gameEntity3 = new GameEntity(null, 3L, 0L, false, false, null, 0L);

        GameEntity saved1 = gameRepository.save(gameEntity1);
        GameEntity saved2 = gameRepository.save(gameEntity2);
        GameEntity saved3 = gameRepository.save(gameEntity3);

        gameRepository.deleteAll();

        Assertions.assertNull(gameRepository.findById(saved1.getId()));
        Assertions.assertNull(gameRepository.findById(saved2.getId()));
        Assertions.assertNull(gameRepository.findById(saved3.getId()));
    }
}