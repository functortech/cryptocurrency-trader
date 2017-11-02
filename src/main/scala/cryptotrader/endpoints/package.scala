package cryptotrader

import io.finch._

import cryptotrader.model._

package object endpoints {
  def authenticatedUser: Endpoint[Option[UserData]] =
    header("x-access-token") map { id =>
      db.user.get(id.toInt)
    }

  def msg(str: String) = Ok(Map("message" -> str))
  def err(str: String) = BadRequest(new RuntimeException(str))
}
