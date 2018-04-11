name := "Testing Scala"

version := "1.0"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test withSources() withJavadoc(),
  "junit" % "junit" % "4.12" % Test withSources() withJavadoc(),
  "org.testng" % "testng" % "6.14.3" % Test withSources() withJavadoc()
)