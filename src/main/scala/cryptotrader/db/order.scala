package cryptotrader.db

import cryptotrader.model._

import com.twitter.util.Future

object order extends MemoryDb[Order] {
  
  def place(o: PlaceOrderReq): Order = {
    val id = nextId()
    val newOrder = o match {
      case SellReq(owner, btc, usd) => Sell(id, owner, btc, usd)
      case BuyReq (owner, btc, usd) => Buy (id, owner, btc, usd)
    }
    database(id) = newOrder
    newOrder
  }

  def get(id: Int): Option[Order] = database.get(id)

  def getAsync(id: Int): Future[Option[Order]] = Future { get(id) }

  def list(): List[Order] = database.values.toList

  def delete(id: Int): Unit = database -= id

  def deleteAsync(id: Int): Future[Unit] = Future { delete(id) }
}
