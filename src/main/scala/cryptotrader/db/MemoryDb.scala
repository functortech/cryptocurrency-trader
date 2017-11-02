package cryptotrader.db

trait MemoryDb[T] {
  protected val database = collection.mutable.HashMap[Int, T]()

  protected var currentId = 0

  protected def nextId() = {
    currentId += 1
    currentId
  }
}
