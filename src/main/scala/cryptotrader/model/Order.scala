package cryptotrader.model

sealed trait Order { val id, owner: Int; val btc, usd: Double }
case class Sell(id: Int, owner: Int, btc: Double, usd: Double) extends Order
case class Buy (id: Int, owner: Int, btc: Double, usd: Double) extends Order

case class TradeReq(orderId: Int)

sealed trait PlaceOrderReq { val owner: Int; val btc, usd: Double }
case class SellReq(owner: Int, btc: Double, usd: Double) extends PlaceOrderReq
case class BuyReq (owner: Int, btc: Double, usd: Double) extends PlaceOrderReq