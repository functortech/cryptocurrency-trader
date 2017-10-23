package cryptotrader

import io.finch._
import com.twitter.finagle.Http
import com.twitter.util.Await


object Main {
  val api: Endpoint[String] =
    get("index") {
      Ok("Welcome to the Crypto[Trader] - your ultimate cryptocurrency trader")
    }

  def main(args: Array[String]): Unit = {
    val port = 8081
    println(s"Starting server at port $port")

    Await.ready {
      Http.server.serve(s":$port", api.toServiceAs[Text.Plain])
    }
  }
}
