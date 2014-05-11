package models

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

object QuizzModel {

    /********** Beans **********/
    case class User(uid: Pk[Long] = NotAssigned, name: String, login_token: String) {
        def json = toJson(Map(
            "name" -> toJson(name),
            "login_token" -> toJson(login_token)))
    }

    case class Group(gid: Pk[Long] = NotAssigned, name: String) {
        def json = toJson(Map(
            "gid" -> toJson(gid.get),
            "name" -> toJson(name)))
    }

    case class Tag(name: String) {
        def json = toJson(Map("tag" -> toJson(name)))
    }

    case class Question(qid: Pk[Long] = NotAssigned, text: String,
        gid: Long, owner: Long, upvote: Long, downvote: Long) {
        def json(tags: Seq[Tag]) = toJson(Map(
            "qid" -> toJson(qid.get),
            "text" -> toJson(text),
            "gid" -> toJson(gid),
            "owner" -> toJson(owner),
            "upvote" -> toJson(upvote),
            "downvote" -> toJson(downvote),
            "tags" -> toJson(tags.map(_.json))))
    }

    // TODO: tag to say if was correct or not
    case class Answer(aid: Pk[Long] = NotAssigned, text: String,
        qid: Long, owner: Long, upvote: Long, downvote: Long) {
        def json() = toJson(Map(
            "aid" -> toJson(aid.get),
            "text" -> toJson(text),
            "qid" -> toJson(qid),
            "owner" -> toJson(owner),
            "upvote" -> toJson(upvote),
            "downvote" -> toJson(downvote)))
    }

    /********** Parsers **********/
    val userParser = {
        get[Pk[Long]]("users.uid") ~
            get[String]("users.name") ~
            get[String]("users.login_token") map {
                case id ~ name ~ token => User(id, name, token)
            }
    }

    val groupParser = {
        get[Pk[Long]]("groups.gid") ~
            get[String]("groups.name") map {
                case id ~ name => Group(id, name)
            }
    }

    val tagParser = get[String]("tags.name") map Tag

    val questionParser = {
        get[Pk[Long]]("questions.qid") ~
            get[String]("questions.text") ~
            get[Long]("questions.gid") ~
            get[Long]("questions.owner") ~
            get[Long]("questions.upvote") ~
            get[Long]("questions.downvote") map {
                case id ~ text ~ group ~ owner ~ up ~ down =>
                    Question(id, text, group, owner, up, down)
            }
    }
    
    val answerParser = {
        get[Pk[Long]]("answers.aid") ~
            get[String]("answers.text") ~
            get[Long]("answers.question") ~
            get[Long]("answers.owner") ~
            get[Long]("answers.upvote") ~
            get[Long]("answers.downvote") map {
                case id ~ text ~ question ~ owner ~ up ~ down =>
                    Answer(id, text, question, owner, up, down)
            }
    }
    
    /********** Data access functions **********/

    /**
     * @brief All the users of the database
     *
     * @return A list of all the current users
     */
    def users = DB.withConnection {
        implicit conn => SQL("SELECT * FROM users").as(userParser *)
    }

    /**
     * @brief Lookup a user by name
     *
     * @return Some(user) if user with that name exists or None
     * 			if no user with that name was found
     */
    def userByName(name: String) = DB.withConnection {
        implicit conn =>
            SQL("SELECT * FROM users WHERE name = { name }")
                .on('name -> name)
                .as(userParser.singleOpt)
    }

    /**
     * @brief Lookup a user by login token
     *
     * @return Some(user) if user with that login token exists or None
     * 			if no user with that name was found
     */
    def userByToken(token: String) = DB.withConnection {
        implicit conn =>
            SQL("SELECT * FROM users WHERE login_token = { token }")
                .on('token -> token)
                .as(userParser.singleOpt)
    }

    /**
     * @brief Adds a new user to the database
     *
     * @return true on success, false on failure
     */
    def newUser(user: User) = DB.withConnection {
        implicit conn =>
            SQL(""" 
                    INSERT INTO users 
                    (name, login_token)
                    VALUES( {name}, {login_token} );
                """).on(
                'name -> user.name,
                'login_token -> user.login_token).executeUpdate() == 1
    }

    /**
     * @brief Get all groups a user belongs to
     */
    def groupsFor(user: User) = DB.withConnection {
        implicit conn =>
            SQL("SELECT users.uid, userd.name, users.login_token FROM users WHERE name = { name }")
                .on('name -> user.name)
                .as(userParser *)
    }

}