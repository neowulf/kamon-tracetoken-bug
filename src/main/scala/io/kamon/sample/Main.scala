package io.kamon.sample

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.event.Logging
import akka.io.IO
import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import kamon.trace.Tracer
import kamon.trace.logging.LogbackTraceTokenConverter
import org.json4s.{DefaultFormats, Formats}
import spray.can.Http
import spray.httpx.Json4sJacksonSupport
import spray.routing.{HttpServiceActor, _}

import scala.concurrent.ExecutionContext

class BuildInfoController()(implicit system: ActorSystem, ec: ExecutionContext) extends Directives with Json4sJacksonSupport {
  override implicit val json4sJacksonFormats: Formats = DefaultFormats

  val versionMap = Map[String, String]("version" -> "1.0")

  val log =  Logging(system, getClass)

  val route = {
    path("build") {
      detach(ec) {
        complete {
          log.info("getting build information!")
          versionMap
        }
      }
    }
  }
}

class HttpRequestHandler(routes: Route) extends HttpServiceActor with ActorLogging {

  override def receive: Receive = runRoute(routes)
}

object Main extends App {

  implicit val system = ActorSystem("kamon")
  implicit val ec = system.dispatcher
  implicit val log = system.log

  log.info("initializing!")

  val routes = new BuildInfoController().route

  val rootService = system.actorOf(Props(new HttpRequestHandler(routes)))

  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
}
