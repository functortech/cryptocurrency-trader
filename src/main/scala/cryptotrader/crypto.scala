package cryptotrader

import java.security.MessageDigest


object crypto {
  // https://stackoverflow.com/a/2756627/3895471
  def valueOf(buf: Array[Byte]): String =
    buf.map("%02X" format _).mkString
  
  // https://stackoverflow.com/a/5992852/3895471
  def hashBytes(s: String, algo: String): Array[Byte] =
    MessageDigest.getInstance(algo).digest(s.getBytes)
  
  def hash(s: String, salt: String, algo: String): String =
    valueOf(hashBytes(s + salt, algo)).toLowerCase

  def sha256(s: String, salt: String = "") =
    hash(s, salt, "SHA-256")

  object secret {
    val passwordSalt = "password-salt"
  }
}
