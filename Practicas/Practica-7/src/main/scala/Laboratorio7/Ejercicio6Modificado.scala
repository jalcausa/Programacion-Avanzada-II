package Laboratorio7

import scala.util.Random
import java.util.concurrent.locks
import java.util.concurrent.locks.ReentrantLock

/*
En esta versión es el niño que llega y se encuentra la tarta vacía el que avisa al pastelero
y no el que coge la última ración, necesitamos una condición para que espere el que ha llamado
al pastelero y sea el primero en comer una vez que el pastelero ha dejado la tarta y no se le cuele
ningún otro niño que estuviese esperando
*/

class Bandeja2(R:Int){
  private val l = new ReentrantLock(true)
  private var raciones = 0

  private var bandejaVacia = true
  private var cBandejaVacia = l.newCondition() // pastelero

  private var esperoPastelero = true
  private val cEsperoPastelero = l.newCondition() // para el primer niño

  private var pAbierta = true
  private val cPuertaAbierta = l.newCondition() // resto de niños


  def quieroRacion(id:Int)= {
    l.lock()
    try {
      while (!pAbierta) cPuertaAbierta.await()
      if (raciones == 0) {
        log(s"Bandeja vacía, niño $id avisa al pastelero")
        pAbierta = false
        bandejaVacia = true
        cBandejaVacia.signal()
        while (esperoPastelero) cEsperoPastelero.await()
        esperoPastelero = true
      }
      raciones -= 1
      log(s"Niño $id ha cogido una ración. Quedan $raciones")
      // Una vez que el niño que ha avisado al pastelero ha cogido su trozo, permite comer al resto
      if (raciones == R - 1) {
        pAbierta = true
        cPuertaAbierta.signal()
      }
    } finally {
      l.unlock()
    }
  }
  def tarta()= {
    l.lock()
    try {
      while (!bandejaVacia) cBandejaVacia.await()
      raciones = R
      bandejaVacia = false
      log("El pastelero pone una nueva tarta.!!!")
      esperoPastelero = false
      cEsperoPastelero.signal()
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio6Modificado {

  def main(args:Array[String]):Unit = {
    val R = 5
    val N = 10
    var fin = false
    val bandeja = new Bandeja2(R)
    var niño = new Array[Thread](N)
    for (i<-niño.indices)
      niño(i) = thread{
        for(j<-0 until 5 ){
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
  }


}
