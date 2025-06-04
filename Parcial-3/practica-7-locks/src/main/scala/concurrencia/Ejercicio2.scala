package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.collection.mutable.ListBuffer
import scala.util.Random

class Recursos(rec:Int) {

  private val l = new ReentrantLock(true)
  private val esperaRecursos = l.newCondition()

  private var numRec = rec

  
  def pidoRecursos(id:Int,num:Int) =  {
    //proceso id solicita num recursos
    l.lock()
    try {
      log(s"Proceso $id pide $num recursos.")
      while (num > numRec)
        esperaRecursos.await()
      numRec -= num
      log(s"Proceso $id coge $num recursos. Quedan $numRec")
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
      esperaRecursos.signal()
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
