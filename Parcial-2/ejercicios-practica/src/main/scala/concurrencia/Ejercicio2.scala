package concurrencia

import java.util.concurrent.*
import scala.util.Random

class Cadena(n: Int) {
  // CS-empaquetador-i: espera hasta que hay productos de tipo i
  // CS-colocador: espera si hay n productos en la cadena
  private val tipo = Array.fill(3)(0) // el buffer
  private var cuentaTotal = 0
  private val esperaCol = Semaphore(1) // CS- Colocalor
  def retirarProducto(p: Int) = {
    // ...
    log(s"Empaquetador $p retira un producto. Quedan ${tipo.mkString("[",",","]")}")
    // ...
  }
  def nuevoProducto(p:Int) = {
    // ...
    log(s"Colocador pone un producto $p. Quedan ${tipo.mkString("[",",","]")}")
    log(s"Total de productos empaquetados $cuentaTotal")
    // ...
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
