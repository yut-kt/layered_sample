# Users schema

# --- !Ups

CREATE TABLE posts (
  id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  text varchar(100) NOT NULL,
  parent_post_id varchar(36) DEFAULT NULL,
  posted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY user_id_idx (user_id),
  CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);


# --- !Downs

DROP TABLE IF EXISTS users;
