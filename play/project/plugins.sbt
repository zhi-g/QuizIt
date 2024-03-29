// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" %  System.getProperty("play.version"))

// Coffescript
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0-RC1")

