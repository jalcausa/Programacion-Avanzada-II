package Laboratorio7

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object gestorAgua {
  //CS-Hid1: El hidrógeno que quiere formar una molécula espera si ya hay dos hidrógenos
  //CS-Hid2: Un hidrógeno debe esperar a los otros dos átomos para formar la molécula
  //CS-Ox1: El oxígeno que quiere formar una molécula espera si ya hay un oxígeno
  //CS-Ox2: El oxígeno debe esperar a los otros dos átomos para formar la molécula

  private var nOxigeno = 0
  private var nHidrogeno = 0

  private val l = new ReentrantLock(true)

  private val cHidrogeno = l.newCondition()
  private val cOxigeno = l.newCondition()
  private val cMolecula = l.newCondition()

  def hidrogeno(id: Int) =  {
    //el hidógeno id quiere formar una molécula
    l.lock()
    try {
      while (nHidrogeno == 2)
        cHidrogeno.await()
      log(s"Hidrógeno $id quiere formar una molécula")
      nHidrogeno += 1
      if (nHidrogeno == 2 && nOxigeno == 1) {
        cMolecula.signalAll()
        log(s"      Molécula formada!!!")
        nHidrogeno = 0
        nOxigeno = 0
        cHidrogeno.signalAll()
        cOxigeno.signal()
      }else
        cMolecula.await()
    } finally {
      l.unlock()
    }
  }



  def oxigeno(id: Int) =  {
    //el oxigeno id quiere formar una molécula
    l.lock()
    try {
      while (nOxigeno == 1)
        cOxigeno.await()
      log(s"Oxígeno $id quiere formar una molécula")
      nOxigeno += 1
      if (nHidrogeno == 2 && nOxigeno == 1) {
        cMolecula.signalAll()
        log(s"      Molécula formada!!!")
        nHidrogeno = 0
        nOxigeno = 0
        cHidrogeno.signalAll()
        cOxigeno.signal()
      } else
        cMolecula.await()
    } finally {
      l.unlock()
    }
      //log(s"      Molécula formada!!!")

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
