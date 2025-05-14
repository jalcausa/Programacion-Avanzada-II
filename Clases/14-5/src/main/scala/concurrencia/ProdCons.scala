package concurrencia

import scala.util.Random

class Buffer(N: Int) {
  private val buffer = new Array[Int](N)
  private var i = 0  // índice para el productor
  private var j = 0  // índice para el consumidor
  private var numElem = 0

  // Para el productor
  def almacenar(dato: Int) = synchronized {
    while (numElem == N)
      wait()
    buffer(i) = dato
    i = (i + 1) % N
    numElem += 1
    notify()
    }
  // Para el consumidor
  def extraer(): Int =synchronized {
    while (numElem == 0)
      wait()
    val dato = buffer(j)
    j = (j + 1) % N
    numElem -= 1
    notify()
    dato
  }
}

object ProdCons {
  def main(args: Array[String]): Unit = {
    val b = new Buffer(5)
    val prod = thread {
      for (i <- 0 to 100) {
        Thread.sleep(Random.nextInt(200)) // Tiempo que tarda en producir un dato
        b.almacenar(i)
      }
    }

    val cons = thread {
      for (i <- 0 to 100)
        log(s"Consumidor extrae: ${b.extraer()}")
        Thread.sleep(Random.nextInt(200)) // Consume el dato
    }
  }
}