package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current
import models.QuizzModel._

object Application extends Controller {

    val error = Action { Ok(toJson(Map("error" -> "Simple Error Message"))) }

    /**
     * @brief Gets a list of the groups a user belongs to
     *
     * @input { token : text }
     *
     * @output { groups : [ gid : int , name : text ]* }
     *
     * @error { error : text }
     */
    def getListOfGroups = error

    /**
     * @brief Gets list of questions for a group
     *
     * @input { token : text , gid : int }
     *
     * @output { questions :
     *                   [ qid : int ,
     *                     text : text ,
     *                     gid : int ,
     *                     owner : int ,
     *                     upvote : int ,
     *                     downvote : int ,
     *                     tags : [ tag : text ]+ ]* }
     *
     * @error { error : text }
     */
    def getListOfQuestions = error

    /**
     * @brief Gets list of answers for a question
     *
     * @input { token : text , qid : int }
     *
     * @output { questions :
     *                   [ aid : int ,
     *                     text : text ,
     *                     qid : int ,
     *                     owner : int ,
     *                     upvote : int ,
     *                     downvote : int ]* }
     *
     * @error { error : text }
     */
    def getListOfAnswers = error

    /**
     * @brief Insert new group
     *
     * @input { token : text , name : text }
     *
     * @output { gid : int }
     *
     * @error { error : text }
     */
    def insertNewGroup = error

    /**
     * @brief Insert new question for a group
     *
     * @input { token : text , gid : int , text : text , tags : [ tag : text ]+ }
     *
     * @output { qid : int }
     *
     * @error { error : text }
     */
    def insertNewQuestion = error

    /**
     * @brief Insert new answer for a question
     *
     * @input { token : text , qid : int , text : text }
     *
     * @output { aid : int }
     *
     * @error { error : text }
     */
    def insertNewAnswer = error

    /**
     * @brief Generate a quizz based on a certain tag
     *
     * @input { token : text , gid : int , tag : text }
     *
     * @output { questions :
     *                   [ aid : int ,
     *                     text : text ,
     *                     owner : int ,
     *                     upvote : int ,
     *                     downvote : int ]* }
     *
     * @error { error : text }
     */
    def generateQuizz = error

    /**
     * @brief Proposes a list of tags for an incomplete tag
     *
     * @input { token : text , tag : text , maxnb : int }
     *
     * @output { tags : [ tag : text ]* }
     *
     * @error { error : text }
     */
    def autoCompleteTag = error

    def index = Action {
        Ok(views.html.index("Your new application is ready."))
    }

    /**
     * @brief Simple action that returns the same Json as received or
     *         a BadRequest result if the request was not json
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