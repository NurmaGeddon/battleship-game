package ru.timur.learning.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.timur.learning.configuration.TestConfig;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.ShotRepository;

import java.awt.*;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ShotRepositoryImplTest {

    private final ShotRepository shotRepository;

    private ShotEntity shotEntity;

    ShotRepositoryImplTest(@Autowired ShotRepository shotRepository) {
        this.shotRepository = shotRepository;
    }

    @BeforeEach
    void setUp() {
        shotRepository.deleteAll();
        shotEntity = new ShotEntity(1L,
                3,
                1,
                new PGpoint(1, 2),
                ShotEntity.Outcome.HIT);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        ShotEntity expected = shotEntity;

        ShotEntity actual = shotRepository.save(expected);

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected,
                shotRepository.findByKey(expected.getGameId(),
                        expected.getShotNum()));
    }

    @Test
    void findByKey() {
        ShotEntity expected = shotEntity;
        shotRepository.save(expected);
        ShotEntity actual = shotRepository.findByKey(expected.getGameId(), expected.getShotNum());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        ShotEntity shotEntity1 = shotEntity;
        ShotEntity shotEntity2 = new ShotEntity(2L,
                2,
                1,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);
        ShotEntity shotEntity3 = new ShotEntity(3L,
                4,
                2,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);

        ShotEntity saved1 = shotRepository.save(shotEntity1);
        ShotEntity saved2 = shotRepository.save(shotEntity2);
        ShotEntity saved3 = shotRepository.save(shotEntity3);

        List<ShotEntity> expected = List.of(saved1, saved2, saved3);
        List<ShotEntity> actual = shotRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteByKey() {
        shotRepository.save(shotEntity);
        Assertions.assertTrue(shotRepository.deleteByKey(shotEntity.getGameId(),
                shotEntity.getShotNum()));
        Assertions.assertNull(shotRepository.findByKey(shotEntity.getGameId(),
                shotEntity.getShotNum()));
    }

    @Test
    void findShotsForPlayer() {
        ShotEntity shotEntity1 = shotEntity;
        ShotEntity shotEntity2 = new ShotEntity(1L,
                2,
                1,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);
        ShotEntity shotEntity3 = new ShotEntity(2L,
                4,
                2,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);

        ShotEntity saved1 = shotRepository.save(shotEntity1);
        ShotEntity saved2 = shotRepository.save(shotEntity2);
        ShotEntity saved3 = shotRepository.save(shotEntity3);


        List<ShotEntity> expected = List.of(saved1, saved2);
        List<ShotEntity> actual = shotRepository.findShotsForPlayer(1L, 1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteAll() {
        ShotEntity shotEntity1 = shotEntity;
        ShotEntity shotEntity2 = new ShotEntity(1L,
                2,
                1,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);
        ShotEntity shotEntity3 = new ShotEntity(2L,
                4,
                2,
                new PGpoint(2, 1),
                ShotEntity.Outcome.HIT);

        ShotEntity saved1 = shotRepository.save(shotEntity1);
        ShotEntity saved2 = shotRepository.save(shotEntity2);
        ShotEntity saved3 = shotRepository.save(shotEntity3);

        shotRepository.deleteAll();

        Assertions.assertNull(shotRepository.findByKey(saved1.getGameId(), saved1.getShotNum()));
        Assertions.assertNull(shotRepository.findByKey(saved2.getGameId(), saved2.getShotNum()));
        Assertions.assertNull(shotRepository.findByKey(saved3.getGameId(), saved3.getShotNum()));
    }

    @Test
    void testShot() throws JsonProcessingException {
        Point point = new Point(1, 1);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(point);
        System.out.println(json);
    }
}