package cryptotrader
package endpoints

import shapeless.{ ::, HNil }

import io.finch._
import io.finch.circe._, io.circe.generic.auto._

import cryptotrader.model._

object order {
  def all = placeOrder :+: listOrders :+: trade :+: deleteOrder

  def root = / :: "order"

  def placeOrder: Endpoint[Order] =
    put(root) :: authenticatedUserWithBalance :: jsonBody[PlaceOrderReq] mapOutput {
      case (user, balance) :: order :: HNil =>
        order match {
          case SellReq(id, btc, usd) if
              id == user.id
          &&  balance.btc >= btc
          => Ok(db.order.place(order))

          case BuyReq(id, btc, usd) if
              id == user.id
          &&  balance.usd >= usd
          => Ok(db.order.place(order))

          case _ => err("Invalid owner id or insufficient funds")
        }
    }

  def listOrders: Endpoint[List[Order]] = get(root) {
    Ok(db.order.list())
  }

  def trade: Endpoint[AnyJson] =
    post(root) :: authenticatedUserWithBalance :: jsonBody[TradeReq] mapOutput {
      case (user, balance) :: tradeReq :: HNil =>
        (for {
          order <- db.order.get(tradeReq.orderId)
          owner <- db.user .get(order.owner)

          myBalance = db.balance.getByUser(user .id)
          trBalance = db.balance.getByUser(owner.id)
        } yield executeOrder(order, owner, myBalance, trBalance) )
        match {
          case Some(resp) => resp
          case None => err("Either order or its owner not found - perhaps the order has expired")
        }
    }

  val ownThisOrder = ValidationRule[Option[Order] :: UserData :: HNil]("have you owning this order") {
    case mOrder :: userData :: HNil =>
      mOrder.exists(_.owner == userData.id) }

  def deleteOrder: Endpoint[AnyJson] =
    delete(root) :: (int.mapAsync(db.order.getAsync) ::
      authenticatedUser should ownThisOrder) mapOutputAsync {
        case Some(order) :: user :: HNil =>
          for {
            _ <- db.order.deleteAsync(order.id)
          } yield msg(s"Successfully deleted trade order ${order.id}")
        }

  def executeOrder(order: Order, owner: UserData
    , myBalance: Balance, trBalance: Balance): Output[AnyJson] =
    order match {
      case Sell(id, _, btc, usd) if
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
        db.order  .delete(id)

        msg(s"Successfully bought $btc BTC for $usd USD")

      case Buy(id, _, btc, usd) if
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
        db.order  .delete(id)


        msg(s"Successfully bought $btc BTC for $usd USD")

      case _ => err("Not enough funds or the order")
    }
}
