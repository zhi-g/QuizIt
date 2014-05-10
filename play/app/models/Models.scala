package models

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(uid: Pk[Long] = NotAssigned, name: String, login_token: String) {
    def json = toJson(Map("name" -> toJson(name), "login_token" -> toJson(login_token)))
}

object QuizzModel {
    val simpleUser = {
        get[Pk[Long]]("users.uid") ~
            get[String]("users.name") ~
            get[String]("users.login_token") map {
                case id ~ name ~ token => User(id, name, token)
            }
    }

    /**
     * @brief All the users of the database
     *
     * @return A list of all the current users
     */
    def users = DB.withConnection {
        implicit conn => SQL("SELECT * FROM users").as(simpleUser *)
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
                .as(simpleUser.singleOpt)
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
            SQL("SELECT * FROM users WHERE name = { name }")
                .on('name -> user.name)
                .as(simpleUser *)
    }

}