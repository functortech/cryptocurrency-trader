package cryptotrader
package endpoints

import io.finch._

import cryptotrader.model._

object balance {
  def all = myBalance

  def root = / :: "balance"

  def myBalance: Endpoint[Balance] =
    get(root) :: authenticatedUser mapOutput { u =>
      Ok(db.balance.getByUser(u.id))
    }
}
