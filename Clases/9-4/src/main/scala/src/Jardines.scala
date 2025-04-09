package src

import java.util.concurrent.*

object cont { // Recurso compartido
  private var n = 0
  private val mutex = new Semaphore(1)
  def inc(): Unit = {
    mutex.acquire()
    n += 1
    mutex.release()
  }
  def valor: Int = n
}

object Jardines {
  def main(args: Array[String]): Unit = {
    val p1 = thread {
      for (i <- 0 until 10)
        cont.inc()
    }

    val p2 = thread {
      for (i <- 0 until 10)
        cont.inc()
    }

    p1.join()
    p2.join()
    log(s"${cont.valor}")
  }
}