create table if not exists account (
    id bigserial primary key,
    login text not null unique,
    password text not null
);

insert into account (login, password)
values ('login', '{bcrypt}$2a$10$9i9QwTNdZASQ9fUkqAvE5u2epITMCroq14ltyYWPNhLdxzDJ/c7nG'),
       ('login1', '{bcrypt}$2a$10$wZ.7Ez0KRehkJUWvos4sMOu9GUESSJKK7z2FF2QTp9AhAmjiuD1i6'),
       ('login2', '{bcrypt}$2a$10$Zns9HkIvJSRHBRMlcyE9yeKJHIpUm9cZSEB91wZBDOr.asnG2nCJi'),
       ('login3', '{bcrypt}$2a$10$BPX3ENFuZje99YHR.esJl.RN2KeO0b5oAMg2u8VM/9WWG9EM2mex.'),
       ('login4', '{bcrypt}$2a$10$r2CEsoDSiD3MrW8gDVAGauh2NtD8vATMB9qoIn/ftLd0Bukhf7wMS'),
       ('login5', '{bcrypt}$2a$10$Ooc.1FzRxSr3eN635PO8u.bfkySjZK63ZES20NKH9nRZF8j4dpB12'),
       ('login6', '{bcrypt}$2a$10$gK9CsVX3l2NClsvxrzaMBuy9dP5BO99aIVfLxK3xUaMggcEabQM9W');
