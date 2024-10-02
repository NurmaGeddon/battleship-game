package ru.timur.learning.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.repository.ShipRepository;
import ru.timur.learning.configuration.TestConfig;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ShipRepositoryImplTest {

    private final ShipRepository shipRepository;

    private ShipEntity shipEntity;

    ShipRepositoryImplTest(@Autowired ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @BeforeEach
    void setUp() {
        shipRepository.deleteAll();
        shipEntity = new ShipEntity(null,
                1L,
                2,
                new PGpoint[]{new PGpoint(1, 1),
                        new PGpoint(1, 2),
                        new PGpoint(1, 3)});
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        ShipEntity expected = shipEntity;

        ShipEntity actual = shipRepository.save(expected);

        expected.setId(actual.getId());

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected, shipRepository.findById(actual.getId()));
    }

    @Test
    void findById() {
        ShipEntity expected = shipRepository.save(shipEntity);
        ShipEntity actual = shipRepository.findById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        ShipEntity shipEntity1 = shipEntity;
        ShipEntity shipEntity2 = new ShipEntity(null,
                2L,
                1,
                new PGpoint[]{new PGpoint(1, 3),
                        new PGpoint(1, 2),
                        new PGpoint(1, 1)});
        ShipEntity shipEntity3 = new ShipEntity(null,
                3L,
                2,
                new PGpoint[]{new PGpoint(3, 3),
                        new PGpoint(2, 2),
                        new PGpoint(1, 1)});

        ShipEntity saved1 = shipRepository.save(shipEntity1);
        ShipEntity saved2 = shipRepository.save(shipEntity2);
        ShipEntity saved3 = shipRepository.save(shipEntity3);

        List<ShipEntity> expected = List.of(saved1, saved2, saved3);
        List<ShipEntity> actual = shipRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
        ShipEntity saved = shipRepository.save(shipEntity);

        ShipEntity expected = new ShipEntity(saved.getId(),
                1L,
                2,
                new PGpoint[]{new PGpoint(3, 1),
                        new PGpoint(2, 2),
                        new PGpoint(1, 3)});
        shipRepository.update(expected);
        ShipEntity actual = shipRepository.findById(saved.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteById() {
        ShipEntity saved = shipRepository.save(shipEntity);
        Assertions.assertTrue(shipRepository.deleteById(saved.getId()));
        Assertions.assertNull(shipRepository.findById(saved.getId()));
    }

    @Test
    void findAllForGame() {
        ShipEntity shipEntity1 = shipEntity;
        ShipEntity shipEntity2 = new ShipEntity(null,
                1L,
                1,
                new PGpoint[]{new PGpoint(1, 3),
                        new PGpoint(1, 2),
                        new PGpoint(1, 1)});
        ShipEntity shipEntity3 = new ShipEntity(null,
                3L,
                2,
                new PGpoint[]{new PGpoint(3, 3),
                        new PGpoint(2, 2),
                        new PGpoint(1, 1)});

        ShipEntity saved1 = shipRepository.save(shipEntity1);
        ShipEntity saved2 = shipRepository.save(shipEntity2);
        ShipEntity saved3 = shipRepository.save(shipEntity3);

        List<ShipEntity> expected = List.of(saved1, saved2);
        List<ShipEntity> actual = shipRepository.findAllForGame(1L);

        Assertions.assertEquals(expected, actual);
    }
}