package controllers

import domain.OutputEvent
import play.api.libs.json._

import javax.inject._
import play.api.mvc._
import services.Dispatcher

@Singleton
class CargoController @Inject()
(
  cc: ControllerComponents,
  dispatcher: Dispatcher
)
  extends AbstractController(cc) {

  def truckArrived: Action[JsValue] =
    process(truck => dispatcher.onTruckArrived(truck))

  def shipmentArrived: Action[JsValue] =
    process(shipment => dispatcher.onShipmentArrived(shipment))

  private def process[T: Reads](dispatch: T => OutputEvent): Action[JsValue] =
    Action(parse.json) { request =>
      request.body.validate[T].fold(
        errors =>
          BadRequest(JsError.toJson(errors)),
        inputEvent =>
          Ok(Json.toJson(dispatch.apply(inputEvent)))
      )
    }
}
