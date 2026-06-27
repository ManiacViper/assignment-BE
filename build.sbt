ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "ice.finance"
ThisBuild / organizationName := "ICE"

lazy val root = (project in file("."))
  .settings(
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    libraryDependencies ++= List(
      "org.typelevel"       %% "cats-core"   % "2.10.0",
      "org.typelevel"       %% "cats-effect" % "3.5.1",
      "co.fs2"              %% s"fs2-core"   % "3.9.2",
      "co.fs2"              %% s"fs2-io"     % "3.9.2",
      "com.disneystreaming" %% "weaver-cats" % "0.8.3" % Test,
      "org.scalatest" %% "scalatest" % "latest.integration" % Test
    ),
//    testFrameworks := Seq(new TestFramework("weaver.framework.CatsEffect"))

  )
