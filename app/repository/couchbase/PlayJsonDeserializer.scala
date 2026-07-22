package repository.couchbase

import com.couchbase.client.scala.codec.JsonDeserializer
import play.api.libs.json.{Json, Reads}

import scala.util.Try

class PlayJsonDeserializer[T: Reads] extends JsonDeserializer[T] {

  override def deserialize(bytes: Array[Byte]): Try[T] =
    Try {
      Json.parse(bytes).as[T]
    }
}
