import java.util.concurrent.*
import scala.util.Random

// IMPLEMENTACIÓN CORRECTA Y COMPLETA DE PRODCONS CON SEMÁFOROS GENERALES
/*
Necesito dos semáforos ya que las condiciones de sincronización son distintas para
los consumidores (hayDato) y los productores (hayEspacio)
* */

class Buffer(N: Int) {
  private val buffer = new Array[Int](N)
  private var i = 0  // índice para el productor
  private var j = 0  // índice para el consumidor
  private val mutex = new Semaphore(1) // para controlar la exclusión mutua
  private val hayDatos = new Semaphore(0) // CS-Consumidor: inicialmente no hay datos en el buffer
  private val hayEspacio = new Semaphore(N) // para el productor, quiero que valga 0 cuando no hay espacio

  // Para el productor
  def nuevoDato(dato: Int): Unit = {
    hayEspacio.acquire()
    mutex.acquire()
    buffer(i) = dato
    i = (i+1)%N
    mutex.release()
    hayDatos.release()
  }
  // Para el consumidor
  def extraeDato(): Int = {
    /*
    En el preprotocolo sí importa el orden de lo acquire ya que
    si entra en su SC el consumidor y no hay datos se bloquearía
    */
    hayDatos.acquire()
    mutex.acquire()
    val dato = buffer(j)
    j = (j + 1)%N
    // En el postprotocolo no importa el orden de las dos instrucciones de abajo
    mutex.release()
    hayEspacio.release()
    dato
  }
}

object ProdCons {
  def main(args: Array[String]): Unit = {
    val b = new Buffer(5)
    val prod = thread {
      for (i <- 0 to 100) {
        Thread.sleep(Random.nextInt(200)) // Tiempo que tarda en producir un dato
        b.nuevoDato(i)
      }
    }

    val cons = thread {
      for (i <- 0 to 100)
        log(s"Consumidor extrae: ${b.extraeDato()}")
        Thread.sleep(Random.nextInt(200)) // Consume el dato
    }
  }
}