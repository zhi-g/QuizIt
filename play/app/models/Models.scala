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
        gid: Long, owner: Long, upvote: Long = 0, downvote: Long = 0) {
        def json(tags: Seq[Tag]) = toJson(Map(
            "qid" -> toJson(qid.get),
            "text" -> toJson(text),
            "gid" -> toJson(gid),
            "owner" -> toJson(owner),
            "upvote" -> toJson(upvote),
            "downvote" -> toJson(downvote),
            "tags" -> toJson(tags.map(_.json))))

        def json = toJson(Map(
            "qid" -> toJson(qid.get),
            "text" -> toJson(text),
            "gid" -> toJson(gid),
            "owner" -> toJson(owner),
            "upvote" -> toJson(upvote),
            "downvote" -> toJson(downvote)))
    }

    // TODO: tag to say if was correct or not
    case class Answer(aid: Pk[Long] = NotAssigned, text: String,
        qid: Long, owner: Long, iscorrect: Boolean, upvote: Long = 0, downvote: Long = 0) {
        def json() = toJson(Map(
            "aid" -> toJson(aid.get),
            "text" -> toJson(text),
            "qid" -> toJson(qid),
            "owner" -> toJson(owner),
            "iscorrect" -> toJson(iscorrect),
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
            get[Boolean]("answers.iscorrect") ~
            get[Long]("answers.upvote") ~
            get[Long]("answers.downvote") map {
                case id ~ text ~ question ~ owner ~ iscorrect ~ up ~ down =>
                    Answer(id, text, question, owner, iscorrect, up, down)
            }
    }

    /********** Data access functions **********/

    /**
     * @brief Get all groups a user belongs to
     */
    def groupsFor(user: User): Seq[Group] = DB.withConnection {
        implicit conn =>
            SQL("""
                    SELECT * 
            		FROM groups JOIN user_group
            		WHERE uid = {id} AND user_group.gid = groups.gid;
                """)
                .on('id -> user.uid)
                .as(groupParser *)
    }

    /**
     * @brief Get all questions in a group
     */
    def questionsForGroup(gid: Long): Seq[Question] = DB.withConnection {
        implicit conn =>
            SQL("""
                    SELECT * 
            		FROM questions 
            		WHERE gid = {id};
                """)
                .on('id -> gid)
                .as(questionParser *)
    }

    /**
     * @brief Get tags for a question
     */
    def tagsForQuestion(qid: Long): Seq[Tag] = DB.withConnection {
        implicit conn =>
            SQL("""
                    SELECT tagname as tags.name
            		FROM question_tag
            		WHERE qid = {id};
                """)
                .on('id -> qid)
                .as(tagParser *)
    }

    /**
     * @brief All the users of the database
     *
     * @return A list of all the current users
     */
    def users = DB.withConnection {
        implicit conn => SQL("SELECT * FROM users").as(userParser *)
    }

    /**
     * @brief All the groups of the database
     *
     * @return A list of all the current groups
     */
    def groups = DB.withConnection {
        implicit conn => SQL("SELECT * FROM groups").as(groupParser *)
    }

    /**
     * @brief All questions of the database
     *
     * @return A list of all the current questions
     */
    def questions = DB.withConnection {
        implicit conn => SQL("SELECT * FROM questions").as(questionParser *)
    }

    /**
     * @brief All the answers of the database
     *
     * @return A list of all the current answers
     */
    def answers = DB.withConnection {
        implicit conn => SQL("SELECT * FROM answers").as(answerParser *)
    }

    /**
     * @brief All the tags in the database
     *
     * @return A list of all the current tags
     */
    def tags = DB.withConnection {
        implicit conn => SQL("SELECT DISTINCT tagname FROM question_tag").as(tagParser *)
    }

    /**
     * @brief Adds a new user to the database
     *
     * @return true on success, false on failure
     */
    def newUser(user: User): Boolean = DB.withConnection {
        implicit conn =>
            SQL("""
            		INSERT INTO users 
            		(name, login_token) 
            		VALUES( {name}, {login_token});
                 """)
                .on(
                    'name -> user.name,
                    'login_token -> user.login_token)
                .executeUpdate() == 1
    }

    /**
     * @brief Adds a new group to the database
     *
     * @return the gid of the newly formed group
     */
    def newGroup(group: Group): Long = DB.withConnection {
        implicit conn =>
            SQL("""
                    INSERT INTO groups 
            		(name) 
            		VALUES( {name} );
                """)
                .on(
                    'name -> group.name)
                .executeInsert(scalar[Long] single);
    }

    /**
     * @brief Adds a new question to a group with its set of tags
     *
     * @return the qid of the newly created question
     */
    def newQuestion(question: Question, tags: Seq[Tag]): Long =
        DB.withConnection {
            implicit conn =>

                /* Insert question */
                val qid = SQL("""
                    INSERT INTO questions 
            		(text, gid, owner ) 
            		VALUES( {text}, {group}, {owner} );
                """)
                    .on(
                        'text -> question.text,
                        'group -> question.gid,
                        'owner -> question.owner)
                    .executeInsert(scalar[Long] single)

                /* Add tags */
                tags.foreach { tag =>
                    SQL("""
                            INSERT INTO question_tag 
                            (tagname, question)
                            VALUES ( {tag}, {qid} );
                            """)
                        .on(
                            'tag -> tag,
                            'qid -> qid)
                        .executeUpdate()
                }

                qid
        }

    /**
     * @brief Adds a new answer to a question
     *
     * @return the aid of the newly created answer
     */
    def newAnswer(answer: Answer): Long =
        DB.withConnection { implicit conn =>
            SQL("""
                    INSERT INTO answers 
            		(text, question, owner, iscorrect ) 
            		VALUES( {text}, {qid}, {owner}, {correct} );
                """)
                .on(
                    'text -> answer.text,
                    'qid -> answer.qid,
                    'owner -> answer.owner,
                    'correct -> answer.iscorrect)
                .executeInsert(scalar[Long] single)
        }

    /**
     * @brief Lookup a user by name
     *
     * @return Some(user) if user with that name exists or None
     * 			if no user with that name was found
     */
    def userByName(name: String) = DB.withConnection {
        implicit conn =>
            SQL("SELECT * FROM users WHERE name = {name}")
                .on('name -> name)
                .as(userParser.singleOpt)
    }

    /**
     * @brief Lookup a user by login token
     *
     * @return Some(user) if user with that login token exists or None
     * 			if no user with that name was found
     */
    def userByToken(token: String): Option[User] = DB.withConnection {
        implicit conn =>
            SQL("SELECT * FROM users " +
                "WHERE login_token = {token}")
                .on('token -> token)
                .as(userParser.singleOpt)
    }

}