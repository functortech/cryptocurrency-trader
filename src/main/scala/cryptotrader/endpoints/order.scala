package cryptotrader
package endpoints

import shapeless.{ ::, HNil }

import io.finch._
import io.finch.circe._, io.circe.generic.auto._

import cryptotrader.model._

object order {
  def all = placeOrder :+: listOrders :+: trade

  def root = / :: "order"

  def placeOrder: Endpoint[Order] =
    put(root) :: authenticatedUserWithBalance :: jsonBody[Order] mapOutput {
      case (user, balance) :: order :: HNil =>
        Ok(db.order.place(user.id, order))
    }

  def listOrders: Endpoint[List[Order]] = get(root) {
    Ok(db.order.list())
  }

  def trade: Endpoint[Balance] =
    post(root) :: authenticatedUserWithBalance :: jsonBody[TradeReq] mapOutput {
      case (user, balance) :: tradeReq :: HNil =>
        val mOrder = db.order.get(tradeReq.id)
        val mOwner = db.user.get(order.owner)

        (mOrder, mOwner).mapN { (order, owner) =>
          val myBalance = db.balance.getByUser(user)
          val trBalance = db.balance.getByUser(order.owner)

          order match {
            case Sell(btc, usd) if
                myBalance.usd >= usd
            &&  trBalance.btc >= btc =>
              val myBalanceNew = myBalance.copy(
                btc = myBalance.btc + btc
              , usd = myBalance.usd - usd)
              val trBalanceNew = trBalance.copy(
                btc = trBalance.btc - btc
              , usd = trBalance.usd + usd)

              db.balance.update(myBalanceNew)
              db.balance.update(trBalanceNew)

              msg(s"Successfully bought $btc BTC for $usd USD")

            case Buy(btc, usd) if
                myBalance.btc >= btc
            &&  trBalance.usd >= usd =>
              val myBalanceNew = myBalance.copy(
                btc = myBalance.btc - btc
              , usd = myBalance.usd + usd)
              val trBalanceNew = trBalance.copy(
                btc = trBalance.btc + btc
              , usd = trBalance.usd - usd)

              db.balance.update(myBalanceNew)
              db.balance.update(trBalanceNew)

              msg(s"Successfully bought $btc BTC for $usd USD")

            case _ => err("Not enough funds or the order")
          }
        } match {
          case Some(resp) => resp
          case None => err("Either order or its owner not found - perhaps the order has expired")
        }
    }
}
