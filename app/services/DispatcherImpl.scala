package services

import domain.{AssignmentCreated, OutputEvent, Shipment, ShipmentWaits, Truck, TruckWaits}
import repository.DispatchingRepository

import javax.inject.{Inject, Singleton}

@Singleton
class DispatcherImpl @Inject()
(
  repository: DispatchingRepository
) extends Dispatcher {

  override def onTruckArrived(truck: Truck): OutputEvent = {
    repository.saveTruck(truck)
    repository.getMaxShipmentForCapacity(truck.capacity) match {
      case Some(shipment) =>
        createAssignment(truck, shipment)
      case None =>
        TruckWaits
    }
  }

  override def onShipmentArrived(shipment: Shipment): OutputEvent = {
    repository.saveShipment(shipment)
    repository.getMinTruckForWeight(shipment.weight) match {
      case Some(truck) =>
        createAssignment(truck, shipment)
      case None =>
        ShipmentWaits
    }
  }

  private def createAssignment(truck: Truck, shipment: Shipment) =
    AssignmentCreated(repository.saveAssignment(truck, shipment))

}
