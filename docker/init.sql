create table if not exists account (
    id bigserial primary key,
    login text not null unique,
    password text not null
);

insert into account (login, password)
values ('login1', 'password1'),
       ('login2', 'password2'),
       ('login3', 'password3'),
       ('login4', 'password4'),
       ('login5', 'password5'),
       ('login6', 'password6');
