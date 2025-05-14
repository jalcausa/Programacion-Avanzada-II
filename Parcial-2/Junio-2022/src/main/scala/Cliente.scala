import java.util.Random

class Cliente(mkt: Supermercado, val user: Int) extends Thread {
  private val r = new Random()

  override def run(): Unit = {
    for (i <- 0 until 2) {
      try {
        Thread.sleep(r.nextInt(2000))
        mkt.nuevoCliente(user)
      } catch {
        case e: InterruptedException => e.printStackTrace()
      }
    }
  }
}