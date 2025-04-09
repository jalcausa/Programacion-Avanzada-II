package src

import java.util.concurrent.*

object cont2 {
  private var n = 0
  private val mutex = new Semaphore(1)
  def inc(): Unit = {
    mutex.acquire()
    n += 1
    mutex.release()
  }
  def valor: Int = n
}

object JardinesArray { // Recurso compartido
  def main(args: Array[String]): Unit = {
    val N = 5
    val p = new Array[Thread](5)
    for (i<-0 until N)
      p(i) = thread {
        for (i <- 0 until 10)
          cont.inc()
      }
    for (i<-0 until N)
      p(i).join()
    log(s"${cont.valor}")
  }
}