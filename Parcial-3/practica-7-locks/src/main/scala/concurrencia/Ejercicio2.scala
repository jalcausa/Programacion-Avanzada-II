package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.collection.mutable.ListBuffer
import scala.util.Random

class Recursos(rec:Int) {

  private val l = new ReentrantLock(true)
  private val esperaRecursos = l.newCondition() // Aquí se bloquea tanto el primero si no tiene recursos como el resto

  private var enEspera = new ListBuffer[Int]() // Cola donde esperan todos los consumidores que van llegando y no son el primero

  private var numRec = rec
  private var nProcesosEsperando = 0 // Para saber si me tengo que esperar si ya hay uno esperando a que le den recursos
  private var sigProceso = -1 // Para que cada hilo sepa cuando le toca, se lo dice el que acaba de salir
  
  def pidoRecursos(id:Int,num:Int) =  {
    //proceso id solicita num recursos
    l.lock()
    try {
      nProcesosEsperando += 1
      log(s"Proceso $id pide $num recursos.")
      if (nProcesosEsperando > 1) {
        enEspera.addOne(id)
        while (sigProceso != id)
          esperaRecursos.await()
      }
      while (num > numRec)
        esperaRecursos.await()
      numRec -= num
      log(s"Proceso $id coge $num recursos. Quedan $numRec")
      nProcesosEsperando -= 1
      if (nProcesosEsperando != 0) {
        sigProceso = enEspera.remove(0)
        esperaRecursos.signalAll()
      } else {
        sigProceso = -1
      }
    } finally {
      l.unlock()
    }
  }

  def libRecursos(id:Int,num:Int) =  {
    //proceso id devuelve num recursos
    l.lock()
    try {
      numRec += num
      log(s"Proceso $id devuelve $num recursos. Quedan $numRec")
      esperaRecursos.signalAll()
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio2 {

  def main(args:Array[String]):Unit = {
    val rec = 5
    val numProc = 10
    val recursos = new Recursos(rec)
    val proceso = new Array[Thread](numProc)
    for (i<-proceso.indices)
      proceso(i) = thread{
      //  while (true){
          val r = Random.nextInt(rec)+1
          recursos.pidoRecursos(i,r)
          Thread.sleep(Random.nextInt(300))
          recursos.libRecursos(i,r)
     //   }
      }
  }
}
