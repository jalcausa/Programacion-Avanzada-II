package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object gestorAgua {
  //CS-Hid1: El hidrógeno que quiere formar una molécula espera si ya hay dos hidrógenos
  //CS-Hid2: Un hidrógeno debe esperar a los otros dos átomos para formar la molécula
  //CS-Ox1: El oxígeno que quiere formar una molécula espera si ya hay un oxígeno
  //CS-Ox2: El oxígeno debe esperar a los otros dos átomos para formar la molécula

  private val l = new ReentrantLock(true)

  // CS-Hidrogeno: un hidrógeno no puede entrar si ya hay dos dentro
  private var nHidrogeno = 0
  private val cHidrogeno = l.newCondition()

  // CS-Oxígeno: un oxígeno no puede entrar si ya hay uno dentro
  private var nOxigeno = 0
  private val cOxigeno = l.newCondition()

  // CS-Ambos: no pueden salir hasta que no se haya formado la molécula que ocurre cuando hay tres átomos dentro
  private val cMolecula = l.newCondition()

  def hidrogeno(id: Int) =  {
    //el hidrógeno id quiere formar una molécula
    l.lock()
    try {
      while (nHidrogeno >= 2) cHidrogeno.await()
      nHidrogeno += 1
      log(s"Hidrógeno $id quiere formar una molécula")
      if (nHidrogeno + nOxigeno == 3) {
        cMolecula.signalAll()
        log(s"      Molécula formada!!!")
        nHidrogeno = 0
        nOxigeno = 0
        cHidrogeno.signalAll()
        cOxigeno.signal()
      } else {
        cMolecula.await()
      }
    } finally {
      l.unlock()
    }
  }



  def oxigeno(id: Int) =  {
    //el oxigeno id quiere formar una molécula
    l.lock()
    try{
      while (nOxigeno >= 1) cOxigeno.await()
      nOxigeno += 1
      log(s"Oxígeno $id quiere formar una molécula")
      if (nHidrogeno + nOxigeno == 3) {
        cMolecula.signalAll()
        log(s"      Molécula formada!!!")
        nHidrogeno = 0
        nOxigeno = 0
        cHidrogeno.signalAll()
        cOxigeno.signal()
      } else {
        cMolecula.await()
      }
    } finally {
      l.unlock()
    }
  }

}
object Ejercicio8 {

  def main(args:Array[String]) =
    val N = 5
    val hidrogeno = new Array[Thread](2*N)
    for (i<-0 until hidrogeno.length)
      hidrogeno(i) = thread{
        Thread.sleep(Random.nextInt(500))
        gestorAgua.hidrogeno(i)
      }
    val oxigeno = new Array[Thread](N)
    for(i <- 0 until oxigeno.length)
      oxigeno(i) = thread {
        Thread.sleep(Random.nextInt(500))
        gestorAgua.oxigeno(i)
      }
    hidrogeno.foreach(_.join())
    oxigeno.foreach(_.join())
    log("Fin del programa")
}
