package controllers

import play.api.mvc._
import play.api.libs.json._
import models.QuizzModel._

object Application extends Controller {

    def jsonError(message: String) = BadRequest(Json.obj("error" -> JsString(message)))

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
            Ok(Json.obj("groups" -> JsArray(groups.map(_.json))))
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
            Ok(Json.obj("questions" ->
                JsArray(questions.map { question =>
                    question.json(tagsForQuestion(question.qid.get))
                })))

        }

        result.getOrElse(jsonError("Something went wrong"))
    }

    /**
     * @brief Gets list of answers for a question
     *
     * @input { token : text , qid : int }
     *
     * @output { answers :
     *                   [ aid : int ,
     *                     text : text ,
     *                     qid : int ,
     *                     owner : int ,
     *                     iscorrect : bool ,
     *                     upvote : int ,
     *                     downvote : int ]* }
     *
     * @error { error : text }
     */
    def getListOfAnswers = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            qid <- (json \ "qid").asOpt[Long];
            user <- authenticateUser(token)
        ) yield {
            val answers = answersForQuestion(qid)
            Ok(Json.obj("answers" -> JsArray(questions.map(_.json))))
        }

        result.getOrElse(jsonError("Something went wrong"))
    }

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
            Ok(Json.obj("gid" -> gid))
        } catch {
            case e: Throwable =>
                e.printStackTrace()
                jsonError(e.getMessage())
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
    def insertNewQuestion = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            gid <- (json \ "gid").asOpt[Int];
            text <- (json \ "text").asOpt[String];
            tags <- Some((json \ "tags" \\ "tag").flatMap(_.asOpt[String]));
            user <- authenticateUser(token)
        ) yield try {
            if (tags isEmpty) {
                jsonError("List of tags should not be empty !")
            } else {
                val question = Question(text = text, gid = gid, owner = user.uid.get)
                val qid: Long = newQuestion(question, tags.map(Tag.apply))
                Ok(Json.obj("qid" -> qid))
            }
        } catch {
            case e: Throwable =>
                e.printStackTrace()
                jsonError(e.getMessage())
        }

        println(request.body)
        println(result)
        result.getOrElse(jsonError("Something went wrong"))
    }

    /**
     * @brief Insert new answer for a question
     *
     * @input { token : text , qid : int , text : text, iscorrect }
     *
     * @output { aid : int }
     *
     * @error { error : text }
     */
    def insertNewAnswer = Action { implicit request =>
        val result = for (
            json <- request.body.asJson;
            token <- (json \ "token").asOpt[String];
            qid <- (json \ "qid").asOpt[Int];
            text <- (json \ "text").asOpt[String];
            iscorrect <- (json \ "iscorrect").asOpt[Boolean];
            user <- authenticateUser(token)
        ) yield try {
            val answer = Answer(text = text, qid = qid,
                owner = user.uid.get, iscorrect = iscorrect)

            val aid: Long = newAnswer(answer)
            Ok(Json.obj("aid" -> aid))

        } catch {
            case e: Throwable =>
                e.printStackTrace()
                jsonError(e.getMessage())
        }

        println(request.body)
        println(result)
        result.getOrElse(jsonError("Something went wrong"))
    }

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
    def autoCompleteTag = Action {
        implicit request =>
            val result = for (
                json <- request.body.asJson;
                token <- (json \ "token").asOpt[String];
                tag <- (json \ "tag").asOpt[String];
                maxnb <- (json \ "maxnb").asOpt[Long];
                user <- authenticateUser(token)
            ) yield {
                val tags = getTagSuggestions(tag, maxnb)
                val jsonTags = JsArray(tags.map(_.json))
                Ok(Json.obj("tags" -> jsonTags))
            }

            print(request)
            println(request.body)
            result.getOrElse(jsonError("Something went wrong"))
    }

    /**
     * @brief Adds a user into a group
     *
     * @input { token : text , gid : int}
     *
     * @output { success : true }
     *
     * @error { error : text }
     */
    def subscribeToGroup = Action {
        implicit request =>
            val result = for (
                json <- request.body.asJson;
                token <- (json \ "token").asOpt[String];
                gid <- (json \ "gid").asOpt[Long];
                user <- authenticateUser(token)
            ) yield {
                if (addToGroup(user, gid))
                    Ok(Json.obj("success" -> true))
                else
                    jsonError("Not able to suscribe user to group")
            }

            print(request)
            println(request.body)
            result.getOrElse(jsonError("Something went wrong"))
    }

    /**
     * @brief Removes a user from a group
     *
     * @input { token : text , gid : int}
     *
     * @output { success : true }
     *
     * @error { error : text }
     */
    def unsubscribeFromGroup = Action {
        implicit request =>
            val result = for (
                json <- request.body.asJson;
                token <- (json \ "token").asOpt[String];
                gid <- (json \ "gid").asOpt[Long];
                user <- authenticateUser(token)
            ) yield {
                if (removeFromGroup(user, gid))
                    Ok(Json.obj("success" -> true))
                else
                    jsonError("Not able to suscribe user to group")
            }

            print(request)
            println(request.body)
            result.getOrElse(jsonError("Something went wrong"))
    }

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
        Ok(Json.prettyPrint(Json.obj(
            "users" -> JsArray(users.map(_.json)),
            "groups" -> JsArray(groups.map(_.json)),
            "questions" -> JsArray(questions.map(_.json)),
            "answers" -> JsArray(answers.map(_.json)),
            "tags" -> JsArray(tags.map(_.json)))))
    }

}