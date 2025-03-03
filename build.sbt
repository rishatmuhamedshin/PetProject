name := "PetProject"

organization := "ru.otus"

val sharedSettings = List{
  version := "0.1"
  scalaVersion := "2.11.5"
}

lazy val db = Project(id = "db", base = file("module/db"))
  .settings(sharedSettings :_*)
  .settings(
    libraryDependencies += "com.typesafe" % "config" % "1.4.3",
    libraryDependencies ++= Seq(
      "org.liquibase" % "liquibase-core" % "3.4.2",
      "org.squeryl" %% "squeryl" % "0.9.5-7",
      "com.zaxxer" % "HikariCP" % "4.0.1",
      "org.postgresql" % "postgresql" % "42.3.1"
    ))

lazy val di = Project(id = "di", base = file("module/di"))
  .settings(sharedSettings :_*)
  .settings(libraryDependencies += Dependencies.Guice)

lazy val auth = Project(id = "auth", base = file("module/auth"))
  .dependsOn(di)
  .settings(sharedSettings :_*)
  .settings(libraryDependencies += Dependencies.JWT)
  .enablePlugins(PlayScala)

lazy val scr = Project(id = "scr", base = file("module/scr"))
  .dependsOn(auth, di, db)
  .settings(sharedSettings :_*)
  .enablePlugins(PlayScala)

lazy val root = (project in file("."))
  .settings(sharedSettings :_*)
  .dependsOn(scr)
  .aggregate(auth, scr, di)
  .enablePlugins(PlayScala)