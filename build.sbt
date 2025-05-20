name := "StudentPubRecommender"

version := "0.2.0"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"       % "2.9.0",
  "org.typelevel" %% "cats-effect"     % "3.5.0",
  "co.fs2"        %% "fs2-core"        % "3.5.0",
  "com.typesafe"  %  "config"          % "1.4.2",
  "ch.qos.logback" % "logback-classic" % "1.4.7",
  "io.circe"      %% "circe-core"      % "0.14.5",
  "io.circe"      %% "circe-generic"   % "0.14.5",
  "io.circe"      %% "circe-parser"    % "0.14.5",
  "org.apache.pdfbox" % "pdfbox"       % "2.0.29"
)
