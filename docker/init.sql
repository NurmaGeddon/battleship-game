create table if not exists account (
    id bigserial primary key,
    login text not null unique,
    password text not null,
    board_id bigserial
);

insert into account (login, password, board_id)
values ('login1', 'password1', 1),
    ('login2', 'password2', 2),
    ('login3', 'password3', 3),
    ('login4', 'password4', 4),
    ('login5', 'password5', 5),
    ('login6', 'password6', 6)
