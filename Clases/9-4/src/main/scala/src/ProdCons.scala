package src

import java.util.concurrent.Semaphore
import scala.util.Random

/*
Necesito dos semáforos ya que las condiciones de sincronización son distintas para
los consumidores y los productores (hayEspacio)
* */

class Buffer(N: Int) {
  val b = new Array[Int](N)
  var i = 0  // índice para el productor
  var j = 0  // índice para el consumidor
  val hayEspacio = new Semaphore(N) // para el productor
  val hayDatos = new Semaphore(0) // CS-Consumidor
  
  def almacenar(dato: Int): Unit = {}
    // para el productor
  def extraer(): Int = 0
    // para el consumidor
}

object ProdCons {
  val buf = new Buffer(5)
  val prod = thread{
    for (i<- 0 until 50){
      Thread.sleep(Random.nextInt(200)) // Tiempo que tarda en producir un dato
      buf.almacenar(i)
    }
  }
  
  val cons = thread{
    for (i<- 0 until 50)
      val dato = buf.extraer()
      Thread.sleep(Random.nextInt(200)) // Consume el dato
  }
}