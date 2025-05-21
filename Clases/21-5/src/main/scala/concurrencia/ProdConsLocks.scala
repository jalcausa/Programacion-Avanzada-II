package concurrencia

import scala.util.Random
import java.util.concurrent.locks._

class Buffer(N: Int) {
  private val l = new ReentrantLock(true) // EL true hace que se esperen en FIFO
  private val buffer = new Array[Int](N)
  private var i = 0  // índice para el productor
  private var j = 0  // índice para el consumidor
  private var numElem = 0
  private val noVacio = l.newCondition() // CS-Consumidor
  private val noLleno = l.newCondition() // CS-Productor

  // Para el productor
  def almacenar(dato: Int) = {
    l.lock()
    try {
      while (numElem == N)
        noLleno.await()
      buffer(i) = dato
      i = (i + 1) % N
      numElem += 1
      if (numElem == 1)
        noVacio.signal()
    } finally {
      l.unlock()
    }
    }
  // Para el consumidor
  def extraer(): Int = {
    l.lock()
    try {
      while (numElem == 0)
        noVacio.await()
      val dato = buffer(j)
      j = (j + 1) % N
      numElem -= 1
      if (numElem == N - 1)
       noLleno.signal()
      dato
    } finally {
      l.unlock()
    }
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