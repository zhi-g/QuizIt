package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(uid: Pk[Long] = NotAssigned, name: String, login_token: String) {
    def json = toJson(Seq(toJson(name), toJson(login_token)))
}

object Application extends Controller {

    val simple = {
        get[Pk[Long]]("users.uid") ~
            get[String]("users.name") ~
            get[String]("users.login_token") map {
                case id ~ name ~ token => User(id, name, token)
            }
    }

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def jsonTest = Action {
        val jsonArray = toJson(Seq(toJson(1), toJson("Bob"), toJson(3), toJson(4)))
        Ok(jsonArray)
    }

    def insertDB = Action {
        DB.withConnection { implicit connection =>
            SQL(
                """
                    insert into users 
                    (name, login_token)
                    values(
                    {name}, {login_token}
                    ),
                    """).on(
                    'name -> "test user",
                    'login_token -> "token").executeUpdate()
        }
        Ok("Ok")
    }

    def getDB = Action {
        DB.withConnection {
            implicit connection =>
                val users = SQL("SELECT * FROM users").as(simple *)
                Ok(toJson(users.map(_.json)))

        }
    }

}