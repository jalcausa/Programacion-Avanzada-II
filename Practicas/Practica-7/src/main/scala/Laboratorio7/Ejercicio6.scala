package Laboratorio7

import scala.util.Random
import java.util.concurrent.locks
import java.util.concurrent.locks.ReentrantLock

class Bandeja(R:Int){
  private val l = new ReentrantLock(true)
  private var raciones = 0

  private var bandejaVacia = true
  private var cBandejaVacia = l.newCondition() // pastelero
  private val cEsperaBandejaVacia = l.newCondition() // niños
  

  def quieroRacion(id:Int)= {
    l.lock()
    try {
      while (bandejaVacia) cEsperaBandejaVacia.await()
      raciones -= 1
      log(s"Niño $id ha cogido una ración. Quedan $raciones")
      if (raciones == 0) {
        bandejaVacia = true
        cBandejaVacia.signal()
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
      log("El pastelero pone una nueva tarta.")
      bandejaVacia = false
      cEsperaBandejaVacia.signalAll()
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio6 {

  def main(args:Array[String]):Unit = {
    val R = 5
    val N = 10
    val bandeja = new Bandeja(R)
    var niño = new Array[Thread](N)
    for (i<-niño.indices)
      niño(i) = thread{
        while (true){
          Thread.sleep(Random.nextInt(500))
          bandeja.quieroRacion(i)
        }
      }
    val pastelero = thread{
      while (true){
        Thread.sleep(Random.nextInt(100))
        bandeja.tarta()
      }
    }
  }


}
