package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

class Bandeja(R:Int){

  private var raciones = 0

  private val l = new ReentrantLock(true)

  // CS-Niños: si hay ración libre, la cogen. El que llega y se la encuentra vacía avisa al pastelero
  // el pastelero se encarga de avisar al niño que le ha avisado de que ya hay tarta para que sea el primero en comer
  private val esperaPrimero = l.newCondition()
  private val esperaResto = l.newCondition()

  // CS-Pastelero: mientras no le digan que está vacía se espera
  private var avisadoPastelero = false // se pone a false para que pueda entrar el primer niño, vea que está vacía y avise
  private val cAvisadoPastelero = l.newCondition()
  

  def quieroRacion(id:Int)= {
    l.lock()
    try {
      if (raciones == 0 && !avisadoPastelero) {
        avisadoPastelero = true
        cAvisadoPastelero.signal()
        log(s"Niño $id avisa al pastelero")
        esperaPrimero.await()
        raciones -= 1
        esperaResto.signalAll()
      }
      else {
        while (avisadoPastelero) esperaResto.await()
        raciones -= 1
      }
      log(s"Niño $id ha cogido una ración. Quedan $raciones")
    } finally {
      l.unlock()
    }
  }
  def tarta()= {
    l.lock()
    try {
      while (!avisadoPastelero) cAvisadoPastelero.await()
      avisadoPastelero = false
      raciones = R
      log("El pastelero pone una nueva tarta.")
      esperaPrimero.signal()
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio6 {

  def main(args:Array[String]):Unit = {
    val R = 5
    val N = 10
    var fin = false
    val bandeja = new Bandeja(R)
    var niño = new Array[Thread](N)
    for (i<-niño.indices)
      niño(i) = thread{
        for(j<-0 until 5){
          Thread.sleep(Random.nextInt(500))
          bandeja.quieroRacion(i)
        }
      }
    val pastelero = thread{
      while (!Thread.interrupted() && !fin){
        try {
          Thread.sleep(Random.nextInt(100))
          bandeja.tarta()
        } catch {
          case e: InterruptedException => fin = true
        }
      }
    }
    niño.foreach(_.join())
    log(s"Los niños se han ido a casa")
    pastelero.interrupt()
    pastelero.join()
    log("fin del programa")
  }


}
