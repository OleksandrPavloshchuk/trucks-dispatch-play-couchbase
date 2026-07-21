package repository

import domain.{Assignment, Shipment, Truck}

trait DispatchingRepository {
  def getMaxShipmentForCapacity(capacity: Double): Option[Shipment]

  def getMinTruckForWeight(weight: Double): Option[Truck]

  def saveAssignment(truck: Truck, shipment: Shipment): Assignment

  def saveTruck(truck: Truck): Unit

  def saveShipment(shipment: Shipment): Unit

}
