package services

import domain.{OutputEvent, Shipment, ShipmentWaits, Truck}

trait Dispatcher {
  def onTruckArrived(truck: Truck): OutputEvent

  def onShipmentArrived(shipment: Shipment): OutputEvent
}
