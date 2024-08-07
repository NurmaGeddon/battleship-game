package ru.timur.learning.repository.impl;

import ru.timur.learning.model.Game;
import ru.timur.learning.repository.GameRepository;

import java.util.List;
import java.util.Optional;

//TODO
public class GameRepositoryImpl implements GameRepository {

    @Override
    public Game save(Game entity) {
        return null;
    }

    @Override
    public Optional<Game> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Game> findAll() {
        return List.of();
    }

    @Override
    public Game update(Game entity) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
