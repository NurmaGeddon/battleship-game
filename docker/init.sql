create table if not exists account (
    id bigserial primary key,
    login text not null unique,
    password text not null
);

create table if not exists game (
    id bigserial primary key,
    player1_id bigint,
    player2_id bigint,
    player1_ready boolean default false,
    player2_ready boolean default false,
    state text check ( state in ('WAITING_FOR_PLAYER',
                                'SHIP_PLACEMENT',
                                'PLAYER1_TURN',
                                'PLAYER2_TURN',
                                'GAME_FINISHED',
                                'CANCELLED') )
                                default 'WAITING_FOR_PLAYER',
    winner_id bigint
);

create table if not exists ship (
    id bigserial primary key,
    game_id bigint not null,
    player_number integer check (player_number in (1, 2)) not null,
    coordinates point[] not null
);

create table if not exists account_game (
    account_id bigint not null,
    game_id bigint not null,
    foreign key (account_id) references account(id),
    foreign key (game_id) references game(id)
);

insert into account (login, password)
values ('login', '{bcrypt}$2a$10$9i9QwTNdZASQ9fUkqAvE5u2epITMCroq14ltyYWPNhLdxzDJ/c7nG'),
       ('login1', '{bcrypt}$2a$10$wZ.7Ez0KRehkJUWvos4sMOu9GUESSJKK7z2FF2QTp9AhAmjiuD1i6'),
       ('login2', '{bcrypt}$2a$10$Zns9HkIvJSRHBRMlcyE9yeKJHIpUm9cZSEB91wZBDOr.asnG2nCJi'),
       ('login3', '{bcrypt}$2a$10$BPX3ENFuZje99YHR.esJl.RN2KeO0b5oAMg2u8VM/9WWG9EM2mex.'),
       ('login4', '{bcrypt}$2a$10$r2CEsoDSiD3MrW8gDVAGauh2NtD8vATMB9qoIn/ftLd0Bukhf7wMS'),
       ('login5', '{bcrypt}$2a$10$Ooc.1FzRxSr3eN635PO8u.bfkySjZK63ZES20NKH9nRZF8j4dpB12'),
       ('login6', '{bcrypt}$2a$10$gK9CsVX3l2NClsvxrzaMBuy9dP5BO99aIVfLxK3xUaMggcEabQM9W');
