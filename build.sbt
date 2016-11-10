
name := "kamon-sample"
organization := "io.kamon.sample"
scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Spray Repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val kamonV = "0.6.3"
  val sprayV = "1.3.3"
  val akkaV = "2.4.10"
  Seq(
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,

    "org.json4s" % "json4s-native_2.11" % "3.4.0",
    "org.json4s" % "json4s-ext_2.11" % "3.4.0",
    "org.json4s" % "json4s-jackson_2.11" % "3.4.0",

    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-client" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-caching" % sprayV,
    "io.spray" %% "spray-json" % "1.3.1",
    
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    
    "io.kamon" %% "kamon-core" % kamonV,
    "io.kamon" %% "kamon-spray" % kamonV,
    "io.kamon" %% "kamon-akka" % kamonV,
    "io.kamon" %% "kamon-akka-remote" % kamonV,
    "io.kamon" %% "kamon-scala" % kamonV,
    "io.kamon" %% "kamon-statsd" % kamonV
  )
}

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

javaOptions ++= Seq(
  "-Dkamon.auto-start=true"
)

fork := true

mainClass in (Compile,run) := Some("io.kamon.sample.Main")