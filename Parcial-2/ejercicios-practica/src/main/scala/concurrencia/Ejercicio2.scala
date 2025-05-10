package concurrencia

import java.util.concurrent.*
import scala.util.Random

class Cadena(n: Int) {
  private val tipo = Array.fill(3)(0) // el buffer
  private var cuentaTotal = 0
  private val mutex = new Semaphore(1)
  private val esperaColocador = new Semaphore(1)
  private val esperaEmpaquetador = new Array[Semaphore](3)
  for (i <- esperaEmpaquetador.indices)
    esperaEmpaquetador(i) = new Semaphore(0)

  def retirarProducto(p: Int) = {
    esperaEmpaquetador(p).acquire()
    mutex.acquire()
    tipo(p) -= 1
    log(s"Empaquetador $p retira un producto. Quedan ${tipo.mkString("[",",","]")}")
    mutex.release()
    esperaColocador.release()
  }
  def nuevoProducto(p:Int) = {
    esperaColocador.acquire()
    mutex.acquire()
    tipo(p) += 1
    cuentaTotal += 1
    log(s"Colocador pone un producto $p. Quedan ${tipo.mkString("[",",","]")}")
    log(s"Total de productos empaquetados $cuentaTotal")
    esperaEmpaquetador(p).release()
    mutex.release()
  }
}

object Ejercicio2 {
  def main(args:Array[String]) = {
    val cadena = new Cadena(6)
    val empaquetador = new Array[Thread](3)
    for (i <- 0 until empaquetador.length)
      empaquetador(i) = thread {
        while (true)
          cadena.retirarProducto(i)
          Thread.sleep(Random.nextInt(500)) // empaquetando
      }

    val colocador = thread {
      while (true)
        Thread.sleep(Random.nextInt(100)) // recogiendo el producto
        cadena.nuevoProducto(Random.nextInt(3))
    }
  }
}
