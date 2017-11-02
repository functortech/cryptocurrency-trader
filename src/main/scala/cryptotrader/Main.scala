package cryptotrader

import com.twitter.finagle.Http
import com.twitter.util.Await

import io.finch._
import io.finch.circe._, io.circe.generic.auto._

import cryptotrader.endpoints._

object Main {

  val api = access.all

  def main(args: Array[String]): Unit = {
    val port = 8081
    println(s"Starting server at port $port")

    Await.ready {
      Http.server.serve(s":$port", api.toServiceAs[Application.Json])
    }
  }
}
