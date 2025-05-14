package concurrencia

import java.util.concurrent.*
import scala.util.Random

class Nido(B: Int) {
  // CS-bebé i: no puede coger un bichito del plato si está vacío
  // CS-papá/mamá: no puede dejar un bichito en el plato si está lleno

  private var plato = 0
  private val mutex = new Semaphore(1)

  private val hayComida = new Semaphore(0)
  private val hayEspacio = new Semaphore(1)

  def cojoBichito(i: Int): Unit = {
    // el bebé i coge un bichito del plato
    hayComida.acquire()
    mutex.acquire()
    plato -=1
    log(s"Bebé $i coge un bichito. Quedan $plato bichitos")
    if (plato > 0)
      hayComida.release()
    if (plato == B - 1)
      hayEspacio.release()
    mutex.release()
  }

  def pongoBichito(i: Int): Unit = {
    // el papá/la mamá pone un bichito en el plato (0=papá, 1=mamá)
    hayEspacio.acquire()
    mutex.acquire()
    plato += 1
    log(s"Papá $i pone un bichito. Quedan $plato bichitos")
    if (plato == 1)
      hayComida.release()
    if (plato < B)
      hayEspacio.release()
    mutex.release()
  }
}

object Ejercicio7 {
  def main(args: Array[String]): Unit =
    val N = 10
    val nido = new Nido(5)
    val bebe = new Array[Thread](N)
    for (i <- bebe.indices)
      bebe(i) = thread {
        while (true)
          nido.cojoBichito(i)
          Thread.sleep(Random.nextInt(600))
      }
    val papa = new Array[Thread](2)
    for (i <- papa.indices)
      papa(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(100))
          nido.pongoBichito(i)
      }
}
