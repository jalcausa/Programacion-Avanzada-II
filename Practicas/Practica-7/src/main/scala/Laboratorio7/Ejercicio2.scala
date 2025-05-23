package Laboratorio7

import scala.collection.mutable.ListBuffer
import scala.util.Random

// CS-1: un proceso espera si hay otros esperando
// CS-2: un proceso espera si es su turno pero no hay recursos suficientes para él
class Recursos(rec:Int) {

  
  private var numRec = rec
  private val enEspera = new ListBuffer[Int]()
  private var procEsperando = 0
  private var sigProceso = -1
  
  def pidoRecursos(id:Int,num:Int) = synchronized {
    //proceso id solicita num recursos
    procEsperando += 1
    log(s"Proceso $id pide $num recursos.")
    if (procEsperando > 1) {
      enEspera.addOne(id)
      while (sigProceso != id) wait() // CS-1
    }
    while (num > numRec) wait() // CS-2
    numRec -= num
    log(s"Proceso $id coge $num recursos. Quedan $numRec")
    procEsperando -= 1
    if (procEsperando != 0){
      sigProceso = enEspera.remove(0)
      notifyAll()
    } else
      sigProceso = -1 // Para que no se vuelva a meter la que tiene id con el valor que se quedó en sigProc
  }

  def libRecursos(id:Int,num:Int) = synchronized {
    //proceso id devuelve num recursos
    numRec += num
    log(s"Proceso $id devuelve $num recursos. Quedan $numRec")
    notifyAll()
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
