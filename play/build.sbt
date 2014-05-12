import play.Project._

name := "QuizIt"

version := "0.1"

scalaVersion := "2.11.0"

playScalaSettings

libraryDependencies ++= Seq(
  jdbc,
  anorm
)