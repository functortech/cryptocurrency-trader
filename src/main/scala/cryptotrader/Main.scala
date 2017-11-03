package cryptotrader

import com.twitter.finagle.Http
import com.twitter.util.Await
import com.twitter.io.Buf

import io.finch._, io.finch.circe._
import io.circe.generic.auto._, io.circe.syntax._

import cryptotrader.endpoints._

object Main {

  val api = access.all :+: balance.all :+: order.all
    
  implicit val encodeExceptionAsJson: Encode.Json[Exception] =
    Encode.json { (e, cs) =>
      Buf.ByteArray.Owned(Map("error" -> Option(e.getMessage)
        .getOrElse("")).asJson.noSpaces.getBytes(cs.name))
    }

  def main(args: Array[String]): Unit = {
    val port = 8081
    println(s"Starting server at port $port")

    Await.ready {
      Http.server.serve(s":$port", api.toServiceAs[Application.Json])
    }
  }
}
