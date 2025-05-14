package concurrencia

import java.util.concurrent.*
import scala.util.Random

object gestorAgua {
  // CS-Hid1: El hidrógeno que quiere formar una molécula espera si ya hay dos hidrógenos
  // CS-Hid2: Un hidrógeno debe esperar a los otros dos átomos para formar la molécula
  // CS-Ox1: El oxígeno que quiere formar una molécula espera si ya hay un oxígeno
  // CS-Ox2: El oxígeno debe esperar a los otros dos átomos para formar la molécula
  private var nHidrogeno = 0
  private val mutex = new Semaphore(1)
  private val puertaOx = new Semaphore(1)
  private val puertaH = new Semaphore(1)
  private val formaMolecula = new Semaphore(0)

  def oxigeno(id: Int) = {
    // el oxígeno id quiere formar una molécula
    puertaOx.acquire()
    log(s"Oxígeno $id quiere formar una molécula")
    formaMolecula.acquire()
    mutex.acquire()
    log(s"      Molécula formada!!!")
    puertaOx.release()
    nHidrogeno = 0
    puertaH.release()
    mutex.release()
  }

  def hidrogeno(id: Int) = {
    // el hidrógeno id quiere formar una molécula
    puertaH.acquire()
    mutex.acquire()
    log(s"Hidrógeno $id quiere formar una molécula")
    nHidrogeno += 1
    if (nHidrogeno == 2)
      formaMolecula.release()
    else
      puertaH.release()
    mutex.release()
  }
}
object Ejercicio5 {

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
