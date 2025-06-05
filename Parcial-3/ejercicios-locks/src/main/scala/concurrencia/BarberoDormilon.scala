package concurrencia

import java.util.concurrent.locks
import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

// CS-1: Un nuevo cliente no se sienta en la silla hasta que el barbero no esté disponible

// CS-2: C2: El barbero no pela al cliente hasta que no se ha sentado en su silla

// CS-3: El cliente no se va hasta que el barbero no le ha abierto la puerta

// CS-4: El barbero no está disponible hasta que el cliente ha cerrado la puerta

object barberia {

  private val l = new ReentrantLock(true)

  private var barberoLibre = false // CS-1
  private val cBarberoLibre = l.newCondition()

  private var ocupadaSilla = false // CS-2
  private val cOcupadaSilla = l.newCondition()

  private var pSalidaAbierta = false // CS-3
  private val cSalidaAbierta = l.newCondition()

  private val cEsperaBarberoCierrePuerta = l.newCondition()

  def pelar(id:Int) = {
    // El cliente id espera su turno para cortarse el pelo
    // IMPORTANTE: justo después de hacer un while() await hay que poner la condición como estaba para reflejar que me he enterado
    l.lock()
    try {
      // Si el barbero está libre paso, si no me espero
      while (!barberoLibre) cBarberoLibre.await() //CS-1
      barberoLibre = false

      // Aviso al barbero de que hay un cliente en la silla
      ocupadaSilla = true // CS-2
      cOcupadaSilla.signal()
      log(s"El cliente $id se sienta en la silla")

      // Me bloqueo hasta que el barbero me abra la puerta
      while (!pSalidaAbierta) cSalidaAbierta.await() // CS-3
      log(s"El cliente $id se marcha")

      // Cierro la puerta y aviso al barbero que he salido
      pSalidaAbierta = false
      cEsperaBarberoCierrePuerta.signal()
    } finally {
      l.unlock()
    }
  }

  def siguiente() = {
    l.lock()
    try {
      // Aviso que estoy libre
      barberoLibre = true // CS-1
      cBarberoLibre.signal()

      /*
       Aquí es donde se va a bloquear el barbero si no hay ningún cliente
       y se va a poder ir a dormir.
       Hasta que el primer cliente que llegue ocupe la silla no se va a despertar.
       EL primer cliente que llega se encuentra al barbero disponible porque antes
       de irse a dormir ha dejado barberoLibre como true por lo que va a poder llegar
       y sentarse en la silla sin problema
      */
      while (!ocupadaSilla) cOcupadaSilla.await()
      ocupadaSilla = false

      log("El barbero pela a un cliente")
    } finally {
      l.unlock()
    }
  }

  def finPelar() = {
    // El barbero pela al cliente que está sentado en la silla
    l.lock()
    try {
      pSalidaAbierta = true
      cSalidaAbierta.signal()

      // Me espero a que el cliente me diga que ha cerrado la puerta y la dejo cerrada
      // En este caso me interesa dejarla así para que el siguiente cliente se la encuentre cerrada y no se vaya antes de tiempo
      while (pSalidaAbierta) cEsperaBarberoCierrePuerta.await()

    } finally {
      l.unlock()
    }
  }
}

object BaberiaPrueba {

  def main(args:Array[String]) = {
    var fin = false
    val barbero = thread {
      while (!Thread.interrupted() && !fin) {
        try {
          barberia.siguiente()
          // Barbero pelando
          Thread.sleep(Random.nextInt(500))
          barberia.finPelar()
        } catch {
          case e:InterruptedException => fin = true
        }
      }
    }
    val cliente = new Array[Thread](100)
    for (i <- 0 until cliente.length) {
      cliente(i) = thread {
        barberia.pelar(i)
      }
    }
    cliente.foreach(_.join())
    log("No hay más clientes.")
    barbero.interrupt()
    barbero.join()
    log("Fin del programa.")
  }
}