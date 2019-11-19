name := "PSmaintry"

version := "0.1"

scalaVersion := "2.11.12"

lazy val flinkVersion = "1.4.0"

lazy val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-java" % flinkVersion
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies,
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22",
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.22",
    // libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.22"
  )