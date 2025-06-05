package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

object Guarderia{
  // Debe verificarse siempre que nBebe<=3*Adulto (3bebes,1adulto),(4bebes,2adulto)
  // CS-1: un bebé que quiere entrar no puede hacerlo hasta que sea cierta la condición nBebe <= 3* nAdulto con él dentro
  // CS-2: un adulto que quiere salir no puede hacerlo hasta que no sea cierta la condición nBebe <= 3* nAdulto con él fuera
  private var nBebe  = 0
  private var nAdulto = 0

  private val l = new ReentrantLock(true)

  private val esperaAdultos = l.newCondition()
  private val esperaBebes = l.newCondition()
  

  def entraBebe(id:Int) =  {
    l.lock()
    try {
      while ((nBebe + 1) > 3*nAdulto) esperaBebes.await()
      nBebe += 1
      assert(nBebe <= 3 * nAdulto)
      log(s"Ha llegado un bebé. Bebés=$nBebe, Adultos=$nAdulto")
   } finally {
     l.unlock()
   }
  }
  def saleBebe(id:Int) =  {
    l.lock()
    try{
      nBebe -= 1
      assert(nBebe <= 3 * nAdulto)
      if (nBebe <= 3*(nAdulto - 1)) esperaAdultos.signal()
      log(s"Ha salido un bebé. Bebés=$nBebe, Adultos=$nAdulto")
    } finally {
      l.unlock()
    }
  }
  def entraAdulto(id:Int) =  {
    l.lock()
    try {
      nAdulto += 1
      assert(nBebe <= 3 * nAdulto)
      esperaAdultos.signal()
      esperaBebes.signalAll()
      log(s"Ha llegado un adulto. Bebés=$nBebe, Adultos=$nAdulto")
    } finally {
      l.unlock()
    }

  }
  def saleAdulto(id:Int) =  {
    l.lock()
    try {
      while (nBebe > 3*(nAdulto - 1)) esperaAdultos.await()
      nAdulto -= 1
      assert(nBebe <= 3 * nAdulto)
      log(s"Ha salido un adulto. Bebés=$nBebe, Adultos=$nAdulto")
    } finally {
      l.unlock()
    }
  }
}
object Ejercicio7 {
  def main(args:Array[String]):Unit={
    val NB = 15
    val NA = 5
    val bebe = new Array[Thread](NB)
    val adulto = new Array[Thread](NA)
    for (i <- bebe.indices)
      bebe(i) = thread {
        while (true) {
          Thread.sleep(Random.nextInt(700))
          Guarderia.entraBebe(i)
          Thread.sleep(Random.nextInt(500))
          Guarderia.saleBebe(i)
        }
      }
    for (i <- adulto.indices)
      adulto(i) = thread {
        while (true) {
          Thread.sleep(Random.nextInt(700))
          Guarderia.entraAdulto(i)
          Thread.sleep(Random.nextInt(500))
          Guarderia.saleAdulto(i)
        }
      }
  }

}
