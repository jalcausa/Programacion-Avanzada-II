package concurrencia

import java.util.concurrent.Semaphore
import scala.util.Random

class Barrera(n: Int) {

  private var esperoTodos = true
  private var sigIter = true
  private var finalizados = 0
  //para sincronizar las iteraciones
  def finIter(id: Int, iter: Int) = synchronized {
    while (!sigIter)
      wait()
    finalizados += 1
    log(s"Worker $id ha terminado la iteración $iter----$finalizados")
    //si soy el último en salir
    log(s"------------------------------------------")
    if (finalizados < n)
      while (esperoTodos)
        wait()
    else {
      sigIter = true
      esperoTodos = false
      notifyAll()
    }

  }
}


object workers {

  def main(args: Array[String]) =
    val N = 5
    val barrera = new Barrera(N)
    val worker = new Array[Thread](N)
    for (i <- 0 until N)
      worker(i) = thread {
        for (j <- 0 until 30)
          Thread.sleep(Random.nextInt(100))
          barrera.finIter(i, j)
      }

}
