package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object Barca{
  private val l = new ReentrantLock(true)

  private var nIPhone = 0
  private var nAndroid = 0

  private var pEntradaIphone = true
  private val cEntradaIphone = l.newCondition()

  private var pEntradaAndroid = true
  private val cEntradaAndroid = l.newCondition()

  private var pSalida = false
  private val cSalida = l.newCondition()

  def paseoIphone(id:Int) =  {
    l.lock()
    try {
      while (!pEntradaIphone) cEntradaIphone.await()
      nIPhone += 1
      log(s"Estudiante IPhone $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
      if (nIPhone + nAndroid == 4) {
        pEntradaIphone = false
        pEntradaAndroid = false
        log(s"Empieza el viaje....")
        Thread.sleep(Random.nextInt(200))
        log(s"fin del viaje....")
        pSalida = true
        cSalida.signalAll()
      }
      else {
        if (nIPhone + nAndroid == 3) {
          if (nIPhone == 1 || nIPhone == 3) pEntradaAndroid = false
          else pEntradaIphone = false
        }
        while (!pSalida) cSalida.await()
      }
      nIPhone -= 1
      log(s"Estudiante IPhone $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
      if (nIPhone + nAndroid == 0) {
        log (s"Se han bajado todos....")
        pSalida = false
        pEntradaIphone = true
        cEntradaIphone.signalAll()
        pEntradaAndroid = true
        cEntradaAndroid.signalAll()
      }
    } finally {
      l.unlock()
    }
  }

  def paseoAndroid(id:Int) =  {
    l.lock()
    try {
      while (!pEntradaAndroid) cEntradaAndroid.await()
      nAndroid += 1
      log(s"Estudiante Android $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
      if (nIPhone + nAndroid == 4) {
        pEntradaIphone = false
        pEntradaAndroid = false
        log(s"Empieza el viaje....")
        Thread.sleep(Random.nextInt(200))
        log(s"fin del viaje....")
        pSalida = true
        cSalida.signalAll()
      }
      else {
        if (nIPhone + nAndroid == 3) {
          if (nAndroid == 1 || nAndroid == 3) pEntradaIphone = false
          else pEntradaAndroid = false
        }
        while (!pSalida) cSalida.await()
      }
      nAndroid -= 1
      log(s"Estudiante Android $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
      if (nIPhone + nAndroid == 0) {
        log (s"Se han bajado todos....")
        pSalida = false
        pEntradaIphone = true
        cEntradaIphone.signalAll()
        pEntradaAndroid = true
        cEntradaAndroid.signalAll()
      }
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio5 {

  def main(args:Array[String]) = {
    val NPhones = 10
    val NAndroid = 10
    val iphone = new Array[Thread](NPhones)
    val android = new Array[Thread](NAndroid)
    for (i<-iphone.indices)
      iphone(i) = thread{
     //   while (true){
          Thread.sleep(Random.nextInt(400))
          Barca.paseoIphone(i)
        //    }
      }
    for (i <- android.indices)
      android(i) = thread {
     //   while (true) {
          Thread.sleep(Random.nextInt(400))
          Barca.paseoAndroid(i)
     //   }
      }
  }
}
