package cryptotrader

import io.finch._

import cryptotrader.model._

package object endpoints {
  type AnyJson = Map[String, String]

  def authenticatedUser: Endpoint[UserData] =
    header("x-access-token") mapOutput { id =>
      db.user.get(id.toInt) match {
        case Some(u) => Ok(u)
        case None => err("Your access token is invalid - please log in again")
      }
    }

  def authenticatedUserWithBalance: Endpoint[(UserData, Balance)] =
    authenticatedUser map { u =>
      u -> db.balance.getByUser(u.id)
    }

  def msg(x: Any     ): Output[AnyJson] = Ok(Map("message" -> x.toString))
  def err(str: String): Output[Nothing] = BadRequest(new RuntimeException(str))
}
