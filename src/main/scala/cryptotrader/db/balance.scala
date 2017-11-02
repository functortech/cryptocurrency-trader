package cryptotrader.db

import cryptotrader.model._


object balance extends MemoryDb[Balance] {
  def getByUser(id: Int): Balance =
    database.collectFirst { case (id, b) if b.owner == id => b } match {
      case Some(b) => b
      case None =>
        val defaultBalance = Balance(owner = id, btc = 1.0, usd = 1000.0)
        database(nextId()) = defaultBalance
        defaultBalance
    }
}
