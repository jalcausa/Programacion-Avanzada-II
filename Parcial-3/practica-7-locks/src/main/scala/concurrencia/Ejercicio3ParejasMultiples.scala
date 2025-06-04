package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object ParejasMultiples{
  // CS-1: un hombre/mujer no puede entrar en la sala si ya hay uno dentro
  // CS-2: un hombre/mujer que está en la sala tiene que esperarse a que se forme la pareja para poder salir

  private val l = new ReentrantLock(true)

  private var hayHombre = false
  private val esperaHombre = l.newCondition()
  private var hayMujer = false
  private val esperaMujer = l.newCondition()
  private var hayPareja = false
  private val esperaPareja = l.newCondition()

  def llegaHombre(id:Int) = {
    l.lock()
    try {
      while (hayHombre) esperaHombre.await()
      log(s"Hombre $id quiere formar pareja")
      hayHombre = true
      if (hayMujer) {
        log(s"Se ha formado una pareja!!")
        hayPareja = true
        esperaPareja.signal()
      } else {
        while (!hayPareja) esperaPareja.await()
        hayPareja = false
      }
      hayHombre = false
      log(s"Sale hombre $id")
      esperaHombre.signal()
    } finally {
      l.unlock()
    }
  }

  def llegaMujer(id: Int) =  {
   l.lock()
    try {
      while (hayMujer) esperaMujer.await()
      log(s"Mujer $id quiere formar pareja")
      hayMujer = true
      if (hayHombre) {
        log(s"Se ha formado una pareja!!")
        hayPareja = true
        esperaPareja.signal()
      } else {
        while (!hayPareja) esperaPareja.await()
        hayPareja = false
      }
      hayMujer = false
      log(s"Sale mujer $id")
      esperaMujer.signal()
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio3Modificado {

  def main(args: Array[String]): Unit = {
    val NP = 10
    val mujer = new Array[Thread](NP)
    val hombre = new Array[Thread](NP)
    for (i <- mujer.indices)
      mujer(i) = thread {
        while (true)
          ParejasMultiples.llegaMujer(i)
      }
    for (i <- hombre.indices)
      hombre(i) = thread {
        while (true)
          ParejasMultiples.llegaHombre(i)
      }
  }
}
