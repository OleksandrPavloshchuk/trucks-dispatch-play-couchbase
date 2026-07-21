package domain

import play.api.libs.json.{Json, OWrites, Reads}

// TODO create validation: weight or capacity can't be less than 0
case class Shipment(name: String, weight: Double)

object Shipment {
  implicit val reads: Reads[Shipment] = Json.reads[Shipment]
  implicit val writes: OWrites[Shipment] = Json.writes[Shipment]
}

case class Truck(name: String, capacity: Double)

object Truck {
  implicit val reads: Reads[Truck] = Json.reads[Truck]
  implicit val writes: OWrites[Truck] = Json.writes[Truck]
}

case class Assignment(truck: Truck, shipment: Shipment)

object Assignment {
  implicit val writes: OWrites[Assignment] = Json.writes[Assignment]
}