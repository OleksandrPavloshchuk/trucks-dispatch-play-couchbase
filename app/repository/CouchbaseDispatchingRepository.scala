package repository

import com.couchbase.client.scala.query.{QueryOptions, QueryParameters}
import domain.{Assignment, Shipment, Truck}
import repository.couchbase.deserialize.{ShipmentDeserializer, TruckDeserializer}

import javax.inject.{Inject, Singleton}

@Singleton
class CouchbaseDispatchingRepository @Inject()
(
  couchbase: CouchbaseProvider
) extends DispatchingRepository {

  override def getMaxShipmentForCapacity(capacity: Double): Option[Shipment] = {
    val statement =
      """
        |SELECT s.*
        |FROM `td-bucket`.`td-scope`.`shipments` AS s
        |LEFT JOIN `td-bucket`.`td-scope`.`assignments` AS a
        |ON a.shipment.name = s.name
        |WHERE s.weight <= $capacity
        |AND a IS MISSING
        |ORDER BY s.weight DESC
        |LIMIT 1;
        |""".stripMargin

    val params = QueryOptions().parameters(
      QueryParameters.Named("capacity" -> capacity))
    val queryResult = couchbase.cluster.query(statement, params).get

    queryResult.rowsAs[Shipment](ShipmentDeserializer).get.headOption
  }

  override def getMinTruckForWeight(weight: Double): Option[Truck] = {
    val statement =
      """
        |SELECT t.*
        |FROM `td-bucket`.`td-scope`.`trucks` AS t
        |LEFT JOIN `td-bucket`.`td-scope`.`assignments` AS a
        |ON a.truck.name = t.name
        |WHERE t.capacity >= $weight
        |AND a IS MISSING
        |ORDER BY t.capacity ASC
        |LIMIT 1;
        |""".stripMargin

    val params = QueryOptions().parameters(
      QueryParameters.Named("weight" -> weight))
    val queryResult = couchbase.cluster.query(statement, params).get

    queryResult.rowsAs[Truck](TruckDeserializer).get.headOption
  }

  override def saveAssignment(truck: Truck, shipment: Shipment): Assignment = {
    val assignment = Assignment(truck, shipment)
    couchbase.save(couchbase.assignments, s"${truck.name}:${shipment.name}", assignment)
    assignment
  }

  override def saveTruck(truck: Truck): Unit =
    couchbase.save(couchbase.trucks, truck.name, truck)

  override def saveShipment(shipment: Shipment): Unit =
    couchbase.save(couchbase.shipments, shipment.name, shipment)

}
