# Tasks schema
 
# --- !Ups

CREATE TABLE users (
    uid INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE, 
    login_token VARCHAR(255)
);

CREATE TABLE groups (
    gid INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_group (
    uid INTEGER NOT NULL,
    gid INTEGER NOT NULL,
    FOREIGN KEY(uid) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY(gid) REFERENCES groups ON DELETE CASCADE
);

CREATE TABLE questions (
    qid INTEGER PRIMARY KEY AUTO_INCREMENT,
    string VARCHAR(255) NOT NULL,
    upvote INTEGER,
    downvote INTEGER
);

    
 
# --- !Downs
 
DROP TABLE questions;
DROP TABLE user_group;
DROP TABLE groups;
DROP TABLE users;
