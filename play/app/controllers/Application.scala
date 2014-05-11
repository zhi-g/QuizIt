package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current
import models.QuizzModel._

object Application extends Controller {

    def jsonError(message: String) = BadRequest(toJson(Map("error" -> message)))

    val ??? = Action { jsonError("Not Implemented") }

    def authenticateUser(token: String) = userByToken(token)

    /**
     * @brief Gets a list of the groups a user belongs to
     *
     * @input { token : text }
     *
     * @output { groups : [ gid : int , name : text ]* }
     *
     * @error { error : text }
     */
    def getListOfGroups = Action { implicit request =>
        //TODO: handle errors better
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            user <- authenticateUser(token)
        ) yield {
            val groups = groupsFor(user)
            Ok(toJson(Map("groups" -> toJson(groups.map(_.json)))))
        }

        result.getOrElse(jsonError("Something went wrong"))
    }

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
    def getListOfQuestions = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            gid <- (json \ "gid").asOpt[Long];
            user <- authenticateUser(token)
        ) yield {
            val questions = questionsForGroup(gid)
            Ok(toJson(Map("questions" ->
                toJson(questions.map { question =>
                    question.json(tagsForQuestion(question.qid.get))
                }))))

        }

        result.getOrElse(jsonError("Something went wrong"))
    }

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
    def getListOfAnswers = ???

    /**
     * @brief Insert new group
     *
     * @input { token : text , name : text }
     *
     * @output { gid : int }
     *
     * @error { error : text }
     */
    def insertNewGroup = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            groupname <- (json \ "name").asOpt[String];
            user <- authenticateUser(token)
        ) yield try {
            val gid = newGroup(Group(name = groupname))
            Ok(toJson(Map("gid" -> gid)))
        } catch {
            case e: Throwable => jsonError(e.getMessage())
        }

        println(request.body)
        println(result)
        result.getOrElse(jsonError("Something went wrong"))
    }

    /**
     * @brief Insert new question for a group
     *
     * @input { token : text , gid : int , text : text , tags : [ tag : text ]+ }
     *
     * @output { qid : int }
     *
     * @error { error : text }
     */
    def insertNewQuestion = ???

    /**
     * @brief Insert new answer for a question
     *
     * @input { token : text , qid : int , text : text }
     *
     * @output { aid : int }
     *
     * @error { error : text }
     */
    def insertNewAnswer = ???

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
    def generateQuizz = ???

    /**
     * @brief Proposes a list of tags for an incomplete tag
     *
     * @input { token : text , tag : text , maxnb : int }
     *
     * @output { tags : [ tag : text ]* }
     *
     * @error { error : text }
     */
    def autoCompleteTag = ???

     /**
     * @brief Adds a user into a group
     *
     * @input { token : text , gid : int}
     *
     * @output { success : true }
     *
     * @error { error : text }
     */
    def subscribeToGroup = ???
    
    /**
     * @brief Removes a user from a group
     *
     * @input { token : text , gid : int}
     *
     * @output { success : true }
     *
     * @error { error : text }
     */
    def unsubscribeFromGroup = ???
    
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
            json <- request.body.asJson.orElse(request.body.asText.map(Json.parse));
            name <- (json \ "name").asOpt[String];
            token <- (json \ "login_token").asOpt[String]
        ) yield {
            val success = newUser(User(name = name, login_token = token))
            Ok("YEAH")
        }

        print(request)
        println(request.body)
        result.getOrElse(Ok(request.body.toString()))

    }

    /**
     *  Returns a json array of all the users in the database
     */
    def allUsers = Action {
        Ok(toJson(Map(
            "users" -> toJson(users.map(_.json)),
            "groups" -> toJson(groups.map(_.json)))))
    }

}