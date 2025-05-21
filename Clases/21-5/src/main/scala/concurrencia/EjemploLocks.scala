package concurrencia
import java.util.concurrent.locks._

object Jardin {
  private var n = 0
  private val l = new ReentrantLock(true)

  def inc(): Unit = {
    l.lock()
    try{
      n += 1
    } finally { // Para si ocurre cualquier error devolver el lock
      l.unlock()
    }
  }
}

object EjemploLocks {

}