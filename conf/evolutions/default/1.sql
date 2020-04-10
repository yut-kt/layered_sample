# Users schema

# --- !Ups

CREATE TABLE users (
    id varchar(36) NOT NULL,
    name varchar(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (name)
);

SET @user_1 = UUID();
SET @user_2 = UUID();
SET @user_3 = UUID();

INSERT INTO users(id, name) VALUES
     (@user_1, 'alice')
    ,(@user_2, 'bob')
    ,(@user_3, 'charlie');

# --- !Downs

DROP TABLE IF EXISTS users;
