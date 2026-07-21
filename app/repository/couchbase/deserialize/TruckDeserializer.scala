package repository.couchbase.deserialize

import com.couchbase.client.scala.codec.JsonDeserializer
import domain.Truck
import play.api.libs.json.Json

import scala.util.Try

object TruckDeserializer extends JsonDeserializer[Truck] {

  override def deserialize(bytes: Array[Byte]): Try[Truck] =
    Try {
      Json.parse(bytes).as[Truck]
    }
}
