val ScalaVer = "2.12.4"

val Finch = "0.16.0-RC1"
val Circe = "0.8.0"

lazy val commonSettings = Seq(
  name    := "crypto-trader"
, version := "0.1.0"
, scalaVersion := ScalaVer
, libraryDependencies ++= Seq(
    "com.github.finagle" %% "finch-core"  % Finch
  , "com.github.finagle" %% "finch-circe" % Finch

  , "io.circe" %% "circe-core"    % Circe
  , "io.circe" %% "circe-generic" % Circe
  , "io.circe" %% "circe-parser"  % Circe
  )

, scalacOptions ++= Seq(
      "-deprecation"
    , "-encoding", "UTF-8"
    , "-feature"
    , "-language:existentials"
    , "-language:higherKinds"
    , "-language:implicitConversions"
    , "-language:experimental.macros"
    , "-unchecked"
    // , "-Xfatal-warnings"
    , "-Xlint"
    // , "-Yinline-warnings"
    , "-Ywarn-dead-code"
    , "-Xfuture"
    , "-Ypartial-unification")
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    initialCommands := "import cryptotrader._, Main._"
  )
