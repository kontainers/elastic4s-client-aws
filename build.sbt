import sbt.Keys._

val awsSdkVersion = "2.17.249"
val elastic4sVersion = "8.2.1"

lazy val buildSettings = Seq(
  organization := "io.kontainers",
  scalaVersion := "2.13.8",
  crossScalaVersions := Seq("2.12.16", scalaVersion.value)
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  Test/ publishArtifact := false,
  versionScheme := Some("early-semver"),
  homepage := Some(new URL("https://github.com/kontainers/elastic4s-client-aws")),
  licenses := Seq(("Apache License 2.0", new URL("http://www.apache.org/licenses/LICENSE-2.0.html"))),
  startYear := Some(2021),
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging),
  pomIncludeRepository := { x => false },
  pomExtra := (
      <issueManagement>
        <system>github</system>
        <url>https://github.com/kontainers/elastic4s-client-aws/issues</url>
      </issueManagement>
      <developers>
        <developer>
          <id>sksamuel</id>
          <name>Sam Samuel</name>
          <url>https://github.com/sksamuel</url>
        </developer>
        <developer>
          <id>sentenza</id>
          <name>Alfredo Torre</name>
          <url>https://github.com/sentenza</url>
        </developer>
      </developers>
    )
)

lazy val elastic4sClientAWS = project.in(file(".")).
  settings(buildSettings).
  settings(publishSettings).
  settings(
    name := "elastic4s-client-aws",
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.7.0",
      "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
      "com.sksamuel.exts" %% "exts" % "1.61.1",
      "org.slf4j" % "slf4j-api" % "2.0.0",
      "software.amazon.awssdk" % "auth" % awsSdkVersion,
      "software.amazon.awssdk" % "core" % awsSdkVersion,
      "software.amazon.awssdk" % "regions" % awsSdkVersion,
      "org.scalatest" %% "scalatest" % "3.2.13" % Test
    )
  )

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / crossScalaVersions := Seq("2.12.16", "2.13.8")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11.0.13"))
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches:= Seq()
