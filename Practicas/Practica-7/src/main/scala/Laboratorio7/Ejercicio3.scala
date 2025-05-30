package Laboratorio7

import scala.util.Random
import java.util.concurrent.locks
import java.util.concurrent.locks.ReentrantLock

/*
  cHombre: se usa el booleano hayHombre
  cMujer: igual
  cPareja: se usa hayPareja para ver si el hombre tiene que esperar a la mujer o vs
*/
object Parejas{

  private val l = new ReentrantLock(true)

  private var hayHombre = false
  private val cHayHombre = l.newCondition()

  private var hayMujer = false
  private val cHayMujer = l.newCondition()

  private var hayPareja = false
  private val cHayPareja = l.newCondition()

  private var n = 0 // n de personas que hay

  def llegaHombre(id:Int) = {
    l.lock()
    try {
      while (hayHombre) cHayHombre.await()
      hayHombre = true
      n += 1
      log(s"Hombre $id quiere formar pareja")
      if (n < 2) {
        while(!hayPareja) cHayPareja.await()
      } else {
        log("Se ha formado la pareja!!!")
        hayPareja = true
        cHayPareja.signal()
      }
      n -= 1
      if (n == 0) {
        hayPareja = false
        hayMujer = false
        cHayMujer.signal()
        hayHombre = false
        cHayHombre.signal()
      }
    } finally {
      l.unlock()
    }
  }

  def llegaMujer(id: Int) =  {
    l.lock()
    try {
      while (hayMujer) cHayMujer.await()
      hayMujer = true
      n += 1
      log(s"Mujer $id quiere formar pareja")
      if (n < 2) {
        while (!hayPareja) cHayPareja.await()
      } else {
        log("Se ha formado la pareja!!!")
        hayPareja = true
        cHayPareja.signal()
      }
      n -= 1
      if (n == 0) {
        hayPareja = false
        hayMujer = false
        cHayMujer.signal()
        hayHombre = false
        cHayHombre.signal()
      }
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio3 {

  def main(args:Array[String]):Unit = {
    val NP = 10
    val mujer = new Array[Thread](NP)
    val hombre = new Array[Thread](NP)
    for (i<-mujer.indices)
      mujer(i) = thread{
        Parejas.llegaMujer(i)
      }
    for (i <- hombre.indices)
      hombre(i) = thread {
        Parejas.llegaHombre(i)
      }
  }

}
