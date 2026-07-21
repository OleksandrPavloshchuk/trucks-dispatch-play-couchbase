package repository

import com.couchbase.client.scala.{Bucket, Cluster, Collection, Scope}
import play.api.Configuration
import play.api.libs.json.{Json, Writes}

import javax.inject.{Inject, Singleton}

@Singleton
class CouchbaseProvider @Inject()
(
  configuration: Configuration
) {
  private val connectionString =
    configuration.get[String]("couchbase.connectionString")

  private val username =
    configuration.get[String]("couchbase.username")

  private val password =
    configuration.get[String]("couchbase.password")

  val cluster: Cluster =
    Cluster.connect(
      connectionString,
      username,
      password
    ).get

  private val bucket = cluster.bucket("td-bucket")

  private val scope = bucket.scope("td-scope")

  val trucks: Collection = scope.collection("trucks")

  val shipments: Collection = scope.collection("shipments")

  val assignments: Collection = scope.collection("assignments")

  def save[T: Writes](collection: Collection, id: String, value: T): Unit =
    collection.insert(id, Json.toJson(value))
}

/*
Наприклад, твій метод

getMaxShipmentForCapacity(capacity)

майже напевно перетвориться на щось на кшталт

SELECT s.*
FROM shipments AS s
WHERE s.weight <= $capacity
ORDER BY s.weight DESC
LIMIT 1;

А для вантажівок:

SELECT t.*
FROM trucks AS t
WHERE t.capacity >= $weight
ORDER BY t.capacity ASC
LIMIT 1;

/*
import com.couchbase.client.scala.Cluster
import com.couchbase.client.scala.query.QueryOptions

val statement =
  """
    |SELECT s.*
    |FROM shipments AS s
    |WHERE s.weight <= $capacity
    |ORDER BY s.weight DESC
    |LIMIT 1
    |""".stripMargin

val result = cluster.query(
  statement,
  QueryOptions().parameters(JsonObject("capacity" -> capacity))
).get

val shipment =
  result.rowsAs[Shipment].get.headOption
 */

 */