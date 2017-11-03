package cryptotrader.db

import cryptotrader.model._


object balance extends MemoryDb[Balance] {
  def getByUser(uid: Int): Balance =
    database.collectFirst { case (_, b) if b.owner == uid => b } match {
      case Some(b) => b
      case None =>
        val id = nextId()
        val defaultBalance = Balance(id = id, owner = uid, btc = 1.0, usd = 1000.0)
        database(id) = defaultBalance
        defaultBalance
    }

  def update(b: Balance): Unit = database(b.id) = b
}
