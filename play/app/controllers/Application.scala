package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current
import models.QuizzModel._
import models.User

object Application extends Controller {    
        
	
    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    /**
     * @brief Simple action that returns the same Json as received or
     * 		   a BadRequest result if the request was not json
     */
    def jsonTest = Action {
        implicit request =>
            request.body.asJson.map { json =>
                Ok(json)
            }.getOrElse(BadRequest("I only want json"))
    }

    /**
     * @brief Add a user to the database
     */
    def addUser = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            name <- (json \ "name").asOpt[String];
            token <- (json \ "login_token").asOpt[String]
        ) yield {
            val success = newUser(User(name = name, login_token = token))
            Ok(toJson(success))
        }

        result.getOrElse(BadRequest)
    }

    /**
     *  Returns a json array of all the users in the database
     */
    def allUsers = Action {
        Ok(toJson(users.map(_.json)))
    }

}