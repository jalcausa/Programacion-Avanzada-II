package Laboratorio7

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

class Buffer(ncons:Int,tam:Int){
  //ncons-número de consumidores
  //tam-tamaño del buffer
  private val l = new ReentrantLock()

  private val buffer = new Array[Int](tam)
  private var nDatos = 0

  // CS-Consumidor
  private val indices = Array.fill(ncons)(0)
  private val datosPendientes = Array.fill(ncons)(0)
  private val esperaCons = Array.fill(ncons)(l.newCondition())
  private val vecesLeido = Array.fill(tam)(0)

  // CS-Productor
  private var hayEspacio = true
  private val chayEspacio = l.newCondition()

  def nuevoDato(dato:Int) = {
    l.lock()
    try {
      while (!hayEspacio) chayEspacio.await()
      buffer((dato - 1) % tam) = dato
      log(s"Productor almacena $dato: buffer=${buffer.mkString("[", ",", "]")}}")
      nDatos += 1
      if (nDatos == tam)
        hayEspacio = false
      for (i <- 0 until ncons) {
        datosPendientes(i) += 1
        if (datosPendientes(i) == 1)
          esperaCons(i).signal()
      }
    } finally {
      l.unlock()
    }
  }

  def extraerDato(id:Int):Int =  {
    var dato = 0
    l.lock()
    try {
      while (datosPendientes(id) == 0) esperaCons(id).await()
      dato = buffer(indices(id))
      vecesLeido(indices(id)) += 1
      if (vecesLeido(indices(id)) == ncons) {
        buffer(indices(id)) = 0
        vecesLeido(indices(id)) = 0
        nDatos -= 1
        hayEspacio = true
        chayEspacio.signal()
      }
      indices(id) = (indices(id) + 1) % tam
      datosPendientes(id) -= 1
      log(s"Consumidor $id lee $dato: buffer=${buffer.mkString("[", ",", "]")}")
      dato
    } finally {
      l.unlock()
    }
  }
}

object Ejercicio1 {

  def main(args:Array[String]):Unit = {
    val ncons = 4
    val tam = 3
    val nIter = 10
    val buffer  = new Buffer(ncons,tam)
    val consumidor = new Array[Thread](ncons)
    for (i<-consumidor.indices)
      consumidor(i) = thread{
        for (j<-0 until nIter)
          val dato = buffer.extraerDato(i)
          Thread.sleep(Random.nextInt(200))
      }
    val productor = thread{
      for (i<-0 until nIter)
        Thread.sleep(Random.nextInt(50))
        buffer.nuevoDato(i+1)
    }
  }

}
