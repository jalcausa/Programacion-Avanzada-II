package concurrencia

import java.util.concurrent.*
import scala.util.Random

class Olla(R: Int) {
  // CS-caníbal i: no coge una ración de la olla si está vacía
  // CS-cocinero: no cocina un nuevo explorador hasta que la olla está vacía
  private var olla = R // inicialmente llena
  private val mutex = new Semaphore(1)

  private val hayComida = new Semaphore(1)
  private val vacia = new Semaphore(0)

  def racion(i: Int) = {
    // caníbal i coge una ración de la olla
    hayComida.acquire()
    mutex.acquire()
    olla -= 1
    log(s"Caníbal $i coge una ración de la olla. Quedan $olla raciones.")
    if (olla == 0)
      vacia.release()
    else
      hayComida.release()
    mutex.release()
  }

  def dormir = {
    // cocinero espera a que la olla esté vacía
    vacia.acquire()
  }

  def llenarOlla = {
    mutex.acquire()
    olla = R
    log(s"El cocinero llena la olla. Quedan $olla raciones.")
    hayComida.release()
    mutex.release()
  }
}

object Ejercicio8 {
  def main(args: Array[String]): Unit =
    val NCan = 20
    val olla = new Olla(5)
    val canibal = new Array[Thread](NCan)
    for (i <- canibal.indices)
      canibal(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500))
          olla.racion(i)
      }
    val cocinero = thread {
      while (true)
        olla.dormir
        Thread.sleep(500) // cocinando
        olla.llenarOlla
    }
}
