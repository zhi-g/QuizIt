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
    FOREIGN KEY(uid) REFERENCES users(uid) ON DELETE CASCADE,
    FOREIGN KEY(gid) REFERENCES groups(gid) ON DELETE CASCADE
);

CREATE TABLE questions (
    qid INTEGER PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(255) NOT NULL,
    gid INTEGER NOT NULL,
    owner INTEGER NOT NULL,
    upvote INTEGER,
    downvote INTEGER,
    FOREIGN KEY(gid) REFERENCES groups(gid) ON DELETE CASCADE,
    FOREIGN KEY(owner) REFERENCES users(uid) ON DELETE CASCADE
);

CREATE TABLE tags (
    name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE question_tag (
    tagname VARCHAR(255) NOT NULL,
    question INTEGER NOT NULL,
    FOREIGN KEY(tagname) REFERENCES tags(name) ON DELETE CASCADE,
    FOREIGN KEY(question) REFERENCES questions(qid) ON DELETE CASCADE
);

CREATE TABLE answers (
    aid INTEGER PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(255) NOT NULL,
    question INTEGER NOT NULL,
    owner INTEGER NOT NULL,
    upvote INTEGER,
    downvote INTEGER,
    FOREIGN KEY(question) REFERENCES questions(qid) ON DELETE CASCADE,
    FOREIGN KEY(owner) REFERENCES users(uid) ON DELETE CASCADE
);

# --- !Downs

DROP TABLE answers;
DROP TABLE question_tag;
DROP TABLE tags; 
DROP TABLE questions;
DROP TABLE user_group;
DROP TABLE groups;
DROP TABLE users;
