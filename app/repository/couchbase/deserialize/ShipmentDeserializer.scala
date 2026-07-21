package repository.couchbase.deserialize

import com.couchbase.client.scala.codec.JsonDeserializer
import domain.Shipment
import play.api.libs.json.Json

import scala.util.Try

object ShipmentDeserializer extends JsonDeserializer[Shipment] {

  override def deserialize(bytes: Array[Byte]): Try[Shipment] =
    Try {
      Json.parse(bytes).as[Shipment]
    }
}
