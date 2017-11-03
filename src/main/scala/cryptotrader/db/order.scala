package cryptotrader.db

import cryptotrader.model._


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

  def list(): List[Order] = database.values.toList

}
