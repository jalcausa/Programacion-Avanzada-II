import java.util.Random

object Driver {
  def main(args: Array[String]): Unit = {
    val NC = 15
    val r = new Random()

    val mkt: Supermercado = new SupermercadoSemaforos() // Comenta esta línea para probar la versión con monitores
    // val mkt: Supermercado = new SupermercadoMonitores() // Descomenta esta línea para probar la versión con monitores

    val c1 = Array.ofDim[Cliente](NC)
    for (i <- 0 until c1.length) {
      c1(i) = new Cliente(mkt, i)
    }

    for (i <- 0 until c1.length) {
      Thread.sleep(r.nextInt(1500))
      c1(i).start()
    }

    Thread.sleep(r.nextInt(1500))

    val c2 = Array.ofDim[Cliente](NC)
    for (i <- 0 until c2.length) {
      c2(i) = new Cliente(mkt, i + 15)
    }

    for (i <- 0 until c2.length) {
      Thread.sleep(r.nextInt(1500))
      c2(i).start()
    }

    /* for (i <- 0 until c.length) {
      c(i).join()
    } */

    Thread.sleep(2000)
    mkt.fin()
  }
}