name := "akka-sample"

organization := "com.asmasa.akka"

version := "1.0.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % "2.3.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.7"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4" % "test"
)
