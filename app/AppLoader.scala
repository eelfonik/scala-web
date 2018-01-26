// our main controller
import controllers.Application

import play.api.ApplicationLoader.Context
import play.api._
// what's this ???
import play.api.libs.ws.ahc.AhcWSComponents
import scala.concurrent.Future

import play.api.mvc._
// where did router come from?
import router.Routes
import play.api.routing.Router
// the DI pack
import com.softwaremill.macwire._
// what's _root_???
import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import akka.actor._
import service.{SunService, WeatherService}
import filters.{StatsFilter}
import actors.{StatsActor}

// ApplicationLoader is a builtin trait
class AppApplicationLoader extends ApplicationLoader {
  // this load method will be invoked by Play everytime the app is reloaded
  def load(context: Context) = {
    // LoggerConfigurator must be sth imported with wildcard
    // but which one??? I guess it's play.api.mvc
    LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
      cfg.configure(context.environment)
    }
    // where did this .application come from ?
    new AppComponents(context).application
  }
}

// WSClient will no longer be injected by Guice (no Guice anymore)
// so we need to add an instance of WSClient manually by extending AhcWSComponents trait
// and later in Application controller we remove @Inject()
class AppComponents(context: Context) extends BuiltInComponentsFromContext(context)
with AhcWSComponents with AssetsComponents with HttpFiltersComponents {
  lazy val statsActor = actorSystem.actorOf(Props(wire[StatsActor]), StatsActor.name)
  val onStart = {
    Logger.info("app is starting")
    // what does the ! means here?
    // it's the tell (fire and forget) pattern
    // in order to get a response from an actor, we need to use ? method -> the ask parttern
    statsActor ! StatsActor.Ping
  }
  override lazy val controllerComponents = wire[DefaultControllerComponents]

  // as we extended the AhcWSComponents trait
  // wsClient is available without actually create an WSClient instance
  // lazy val sunService = new SunService(wsClient)
  // lazy val weatherService = new WeatherService(wsClient)
  
  // A better way of using the wire macro,
  // as it will be replaced with a real instantiation when compiling
  // thus makes the argument wsClient passed automatically
  lazy val sunService = wire[SunService]
  lazy val weatherService = wire[WeatherService]

  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]

  // override the httpFilters 
  lazy val statsFilter: Filter = wire[StatsFilter]
  override lazy val httpFilters = Seq(statsFilter)

  // as we extends BuiltInComponentsFromContext
  // so we have some lifecycle field that can run some funcs when application starts/stops
  applicationLifecycle.addStopHook { () =>
    Logger.info("the app is about to stop")
    Future.successful(Unit)
  }
}