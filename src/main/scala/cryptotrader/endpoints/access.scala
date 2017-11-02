package cryptotrader
package endpoints

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._

import cryptotrader.model._
import crypto._

object access {
  def all = login :+: logout :+: signup

  def root = / :: "access"

  def login =
    post(root) :: jsonBody[LoginReq] mapOutput { case LoginReq(login, passwd) =>
      db.user.login(login, sha256(passwd, secret.passwordSalt)) match {
        case Some(u) => Ok(u)
        case None => err("Incorrect login or password")
      }
    }

  def signup =
    post(root :: "signup") :: jsonBody[SignupReq] mapOutput { case SignupReq(login, password) =>
      val u = db.user.register(login, sha256(password, secret.passwordSalt))
      Ok(u)
    }

  def logout =
    delete(root) :: authenticatedUser mapOutput { u =>
      msg(s"You have logged out, $u. Please delete your session")
    }
}
