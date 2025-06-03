package Laboratorio7

import java.util.concurrent.locks.{Condition, ReentrantLock}
import scala.util.Random

class Buffer(ncons:Int,tam:Int){
  //ncons-número de consumidores
  //tam-tamaño del buffer

  private val buffer = new Array[Int](tam)
  private var indexCons = 0
  private var nDatos = 0

  private val l = new ReentrantLock(true)

  // CS-Productor
  private var hayEspacio = true
  private val chayEspacio = l.newCondition()

  // CS.Consumidor
  private var hayDato = false
  private val chayDato = l.newCondition()

  private val vecesLeido = new Array[Int](tam)

  def nuevoDato(dato:Int) = {
    //el productor pone un nuevo dato
    l.lock()
    try {
      if (nDatos % tam == 0) {
        while (!hayEspacio) chayEspacio.await()
        hayEspacio = false
      }

      buffer((dato - 1) % tam) = dato
      log(s"Productor almacena $dato: buffer=${buffer.mkString("[", ",", "]")}}")
      nDatos += 1
      hayDato = true
      chayDato.signalAll()
    } finally {
      l.unlock()
    }
  }

  def extraerDato(id:Int):Int =  {
    l.lock()
    var dato = 0
    try {
      while(!hayDato) chayDato.await()
      dato = buffer(indexCons % tam)
      vecesLeido(indexCons % tam) += 1

      if (vecesLeido(indexCons % tam) == ncons) {
        buffer(indexCons % tam) = 0
        nDatos -= 1
        if (nDatos == 0)
          hayDato = false
        vecesLeido(indexCons % tam) = 0
        indexCons += 1
        hayEspacio = true
        chayEspacio.signal()
      }
      log(s"Consumidor $id lee $dato: buffer=${buffer.mkString("[", ",", "]")}")
    } finally {
      l.unlock()
    }
    dato
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
