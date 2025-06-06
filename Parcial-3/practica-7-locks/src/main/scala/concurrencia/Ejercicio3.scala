package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object Parejas{
  // CS-1: un hombre/mujer no puede entrar en la sala si ya hay uno dentro
  // CS-2: un hombre/mujer que está en la sala tiene que esperarse a que se forme la pareja para poder salir

  private val l = new ReentrantLock(true)

  private var hayHombre = false
  private val esperaHombre = l.newCondition()
  private var hayMujer = false
  private val esperaMujer = l.newCondition()
  private var hayPareja = false
  private val esperaPareja = l.newCondition()

  private var n = 0 // Número de personas que hay en la sala, se usa para que un mismo hombre/mujer no pueda tener dos citas

  def llegaHombre(id:Int) = {
    l.lock()
    try {
      while (hayHombre) esperaHombre.await()
      log(s"Hombre $id quiere formar pareja")
      hayHombre = true
      n += 1
      if (n == 2) {
        log(s"Se ha formado una pareja!!")
        hayPareja = true
        esperaPareja.signal()
      } else {
        while (!hayPareja) esperaPareja.await()
        hayPareja = false
      }
      n -= 1
      if (n == 0){
        hayPareja = false
        hayHombre = false
        esperaHombre.signal()
        hayMujer = false
        esperaMujer.signal()
      }
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
      n += 1
      if (n == 2) {
        log(s"Se ha formado una pareja!!")
        hayPareja = true
        esperaPareja.signal()
      } else {
        while (!hayPareja) esperaPareja.await()
        hayPareja = false
      }
      n -= 1
      if (n == 0) {
        hayPareja = false
        hayHombre = false
        esperaHombre.signal()
        hayMujer = false
        esperaMujer.signal()
      }
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio3 {

  def main(args: Array[String]): Unit = {
    val NP = 10
    val mujer = new Array[Thread](NP)
    val hombre = new Array[Thread](NP)
    for (i <- mujer.indices)
      mujer(i) = thread {
        Parejas.llegaMujer(i)
      }
    for (i <- hombre.indices)
      hombre(i) = thread {
        Parejas.llegaHombre(i)
      }
  }
}
