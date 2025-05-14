import java.util.Random
import java.util.concurrent.atomic._

class Cajero(mkt: Supermercado, permanente: Boolean) extends Thread {
  import Cajero._

  if (algunoCreado && permanente)
    throw new RuntimeException("Solo el primer cajero puede ser permanente")
  if (!algunoCreado && !permanente)
    throw new RuntimeException("El primer cajero tiene que ser permanente")

  algunoCreado = true
  private val soyPermanente = permanente
  private val id = currentId.getAndIncrement()
  numCajerosCounter.incrementAndGet()

  override def run(): Unit = {
    try {
      if (!soyPermanente) {
        println(s"El nuevo cajero $id comienza a servir a un cliente.")
        Thread.sleep(500 + rnd.nextInt(400))
      }

      if (soyPermanente) {
        while (mkt.permanenteAtiendeCliente(id)) {
          Thread.sleep(500 + rnd.nextInt(400))
        }
      } else {
        while (mkt.ocasionalAtiendeCliente(id)) {
          Thread.sleep(500 + rnd.nextInt(400))
        }
      }
    } catch {
      case e: InterruptedException => e.printStackTrace()
    }
    numCajerosCounter.decrementAndGet()
  }
}

object Cajero {
  private val rnd = new Random()
  private var algunoCreado = false
  private val numCajerosCounter = new AtomicInteger(0)
  private val currentId = new AtomicInteger(0)

  def numCajeros(): Int = numCajerosCounter.get()
}