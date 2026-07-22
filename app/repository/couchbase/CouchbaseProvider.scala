package repository.couchbase

import com.couchbase.client.scala.{Cluster, Collection}
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