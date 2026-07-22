package repository.couchbase

import com.couchbase.client.scala.query.{QueryOptions, QueryParameters}
import domain.{Assignment, Shipment, Truck}
import play.api.libs.json.Reads
import repository.DispatchingRepository

import javax.inject.{Inject, Singleton}

@Singleton
class CouchbaseDispatchingRepository @Inject()
(
  couchbase: CouchbaseProvider
) extends DispatchingRepository {

  override def getMaxShipmentForCapacity(capacity: Double): Option[Shipment] =
    querySingle[Shipment](
      """
        |SELECT s.*
        |FROM `td-bucket`.`td-scope`.`shipments` AS s
        |LEFT JOIN `td-bucket`.`td-scope`.`assignments` AS a
        |ON a.shipment.name = s.name
        |WHERE s.weight <= $capacity
        |AND a IS MISSING
        |ORDER BY s.weight DESC
        |LIMIT 1;
        |""",
      "capacity",
      capacity)

  override def getMinTruckForWeight(weight: Double): Option[Truck] =
    querySingle[Truck](
      """
        |SELECT t.*
        |FROM `td-bucket`.`td-scope`.`trucks` AS t
        |LEFT JOIN `td-bucket`.`td-scope`.`assignments` AS a
        |ON a.truck.name = t.name
        |WHERE t.capacity >= $weight
        |AND a IS MISSING
        |ORDER BY t.capacity ASC
        |LIMIT 1;
      """,
      "weight",
      weight
    )

  private def querySingle[T: Reads]
  (
    statement: String,
    varName: String,
    value: Double
  ): Option[T] = {
    val params = QueryOptions().parameters(
      QueryParameters.Named(varName -> value))
    val queryResult = couchbase.cluster.query(statement.stripMargin, params).get

    queryResult.rowsAs[T](new PlayJsonDeserializer[T]).get.headOption
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
