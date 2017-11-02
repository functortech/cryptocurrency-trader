package cryptotrader.model

sealed trait Order
case class Sell(owner: Int, btc: Double, usd: Double) extends Order
case class Buy (owner: Int, btc: Double, usd: Double) extends Order
