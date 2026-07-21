package domain

import play.api.libs.json.{Json, OWrites, Writes}

abstract class OutputEvent(val eventType: String)

object OutputEvent {
  implicit val writes: Writes[OutputEvent] = Writes { event =>
    Json.obj(
      "eventType" -> event.eventType
    )
  }
}

case class AssignmentCreated(assignment: Assignment) extends OutputEvent("assignmentCreated")

object AssignmentCreated {
  implicit val writes: Writes[AssignmentCreated] = Writes { event =>
    Json.obj(
      "eventType" -> event.eventType,
      "assignment" -> event.assignment
    )
  }
}

case object TruckWaits extends OutputEvent("truckWaits")

case object ShipmentWaits extends OutputEvent("shipmentWaits")