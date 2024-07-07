package ru.timur.learning.model;

public record User(
    Long id,
    String login,
    String password,
    Long boardId
) {}