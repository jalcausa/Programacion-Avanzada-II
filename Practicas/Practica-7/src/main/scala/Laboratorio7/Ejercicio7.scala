package Laboratorio7

import scala.util.Random
import java.util.concurrent.locks
import java.util.concurrent.locks.ReentrantLock

object Guarderia{
  private var nBebe  = 0
  private var nAdulto = 0

  //nBebe<=3*Adulto (3bebes,1adulto),(4bebes,2adulto)
  private val l = new ReentrantLock(true)
  private val cAdulto = l.newCondition()
  private val cBebe = l.newCondition()

  def entraBebe(id:Int) =  {
    l.lock()
    try {
      while (nBebe + 1 > 3*nAdulto) cBebe.await()
      nBebe += 1
      assert(nBebe <= 3 * nAdulto)
      log(s"Ha llegado un bebé. Bebés=$nBebe, Adultos=$nAdulto")
    } finally {
      l.unlock()
    }
  }
  def saleBebe(id:Int) =  {
    l.lock()
    try {
      nBebe -= 1
      log(s"Ha salido un bebé. Bebés=$nBebe, Adultos=$nAdulto")
      if (nBebe <= 3 * (nAdulto - 1)) cAdulto.signal() // Solo en caso de que ahora pueda salir un adulto se le notifica
      cBebe.signal()
    } finally {
      l.unlock()
    }

  }
  def entraAdulto(id:Int) =  {
    l.lock()
    try {
      nAdulto += 1
      log(s"Ha llegado un adulto. Bebés=$nBebe, Adultos=$nAdulto")
      cBebe.signalAll() // Al llegar un adulto pueden entrar varios bebés y se mantiene la condición
      cAdulto.signal() // Al entrar un nuevo adulto otro puede que pueda irse
    } finally {
      l.unlock()
    }

  }
  def saleAdulto(id:Int) =  {
    l.lock()
    try {
      while (nBebe > 3*(nAdulto - 1)) cAdulto.await()
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
