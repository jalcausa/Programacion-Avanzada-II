package concurrencia

import java.util.concurrent.*
import scala.util.Random

class Cadena(n: Int) {
  // CS-empaquetador-i: espera hasta que hay productos de tipo i
  // CS-colocador: espera si hay n productos en la cadena
  private val tipo = Array.fill(3)(0) // el buffer
  private var cuentaTotal = 0
  private val esperaCol = Semaphore(1) // CS- Colocador
  private val hayProducto = new Array[Semaphore](3)
  for (i <- hayProducto.indices)
    hayProducto(i) = new Semaphore(0)
  private val mutex = new Semaphore(1)
  
  def retirarProducto(p: Int) = {
    hayProducto(p).acquire()
    mutex.acquire()
    tipo(p) -= 1
    log(s"Empaquetador $p retira un producto. Quedan ${tipo.mkString("[",",","]")}")
    cuentaTotal += 1
    log(s"Total de productos empaquetados $cuentaTotal")
    if (tipo.sum == n-1)
      esperaCol.release()
    if (tipo(p) > 0)
      hayProducto(p).release()
    mutex.release()
  }
  def nuevoProducto(p:Int) = {
    esperaCol.acquire()
    mutex.acquire()
    tipo(p) += 1
    log(s"Colocador pone un producto $p. Quedan ${tipo.mkString("[",",","]")}")
    // Si ha sido el primer producto de ese tipo que he puesto hay que despertar al
    // empaquetador ya que estaba dormido antes
    if (tipo(p) == 1)
      hayProducto(p).release()
    if (tipo.sum < n)
      esperaCol.release()
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
