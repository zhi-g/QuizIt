package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import models.QuizzModel._
import views.html.defaultpages.badRequest

object Application extends Controller {

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    def jsonTest = Action {
        implicit request =>
            request.body.asJson.map { json =>
                Ok(json)
            }.getOrElse(BadRequest("I only want json"))
    }

    def addUser = Action { implicit request =>
        request.body.asJson.map { json =>
            Ok(json)
        }.getOrElse(BadRequest("I only want json"))
    }

    def allUsers = Action {
        Ok(toJson(users.map(_.json)))
    }

}