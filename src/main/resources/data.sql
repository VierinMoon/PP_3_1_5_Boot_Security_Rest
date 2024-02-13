drop table if exists user_roles, roles, users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(255),
    age INT
);
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role_id BIGINT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

insert into users (username, password, age)
values ('admin', '$2a$12$ve2VBNIFLRTa5CLqBgO3vuc2y8XvMu0Q5EghPJSUl7Bqi63PMAkW2', 25);

insert into users (username, password, age)
values ('user', '$2a$12$rZpAi.sTyElFPz7wrPESoeMbSGzRHUMXsoPCNdONstmy0O2ECTUti', 30);

insert into roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into user_roles (user_id, role_id)
values ((select id from users where username = 'admin'),
        (select id from roles where name = 'ROLE_ADMIN'));

insert into user_roles (user_id, role_id)
values ((select id from users where username = 'admin'),
        (select id from roles where name = 'ROLE_USER'));

insert into user_roles (user_id, role_id)
values ((select id from users where username = 'user'),
        (select id from roles where name = 'ROLE_USER'));