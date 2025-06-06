package sushi

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random
object SushiBar{


  /**
   * ----Primera Parte--- Un cliente quiere entrar al SushiBar
   *
   * CS-cliente1: Un cliente que llega al SushiBar y ve que está lleno,
   * o que hay otros clientes esperando, debe esperar para entrar a
   * que TODOS los clientes del grupo actual se hayan marchado
   */

  /**
   *  ----Segunda Parte--- El cliente solicita su comida
   *  CS-cliente2: Un cliente que está sentado en el SushiBar,
   *  debe esperar a que Itamae esté disponible para solicitar
   *  su comida
   *
   *  CS-Itamae1: Itamae debe esperar a que el cliente le haga
   *  su pedido para poder cocinarlo y servirlo
   *
   *  CS-cliente3: El cliente que ha solicitado su pedido,
   *  espera a que se lo sirvan para inclinar su cabeza y agradecer
   *  el trabajo a Itamae
   *
   *  CS-Itamae2: Una vez que Itamae  ha servido un plato, espera
   *  a que el cliente le haga una pequeña reverencia para
   *  pasar a atender a otro cliente, si hay alguno
   *
   */



  private var numCliente = 0

  private val l = new ReentrantLock(true)

  private var hayEsperando = false
  private val cHayEsperando = l.newCondition()

  private var lleno = false
  private val cLleno = l.newCondition()

  private var itamaeDisponible = true
  private val cItamaeDisponible = l.newCondition()

  private var solicitudSushi = false
  private val cSolicitud = l.newCondition()

  private var platoSushi = false
  private val cPlatoSushi = l.newCondition()

  private var reverencia = false
  private val cReverencia = l.newCondition()


  def entraCliente(id:Int) =  {
    l.lock()
    try {
      // Si ve que hay un grupo pone hayEsperando = true y se espera
      if (numCliente + 1 == 6 ) {
        hayEsperando = true
      }
      // Aquí se esperan los clientes si hay un grupo hasta que salga al completo
      while (hayEsperando) {
        log(s"Cliente $id esperando a que el grupo se marche")
        cHayEsperando.await()
      }
      // Aquí se esperan los clientes que estaban esperando si no caben una vez se ha ido el grupo
      while (lleno) cLleno.await()
      numCliente += 1
      log(s"Cliente $id entra en el SushiBar. Hay $numCliente")
      if (numCliente == 5)
        lleno = true
    } finally {
      l.unlock()
    }
  }


  def saleCliente(id:Int)= {
    l.lock()
    try {
      numCliente -= 1
      log(s"Cliente $id sale del SushiBar. Hay $numCliente")
      // Si soy el último del grupo en salir aviso a los que estaban esperando a que saliera el grupo
      if (numCliente == 0) {
        hayEsperando = false
        cHayEsperando.signalAll()
      // Si se queda un hueco tras salir yo aviso a los que estaban esperando porque no había hueco
      } else if (numCliente == 4 && lleno) {
        lleno = false
        cLleno.signalAll()
      }
    } finally {
      l.unlock()
    }

  }

  def clientePideSushi(id:Int)= {
    l.lock()
    try {
      // Espero a que esté disponible el itamae
      while (!itamaeDisponible) cItamaeDisponible.await()
      itamaeDisponible = false
      log(s"\t\t\t\tCliente $id solicita su sushi")
      // Cuando está disponible le pido el sushi
      solicitudSushi = true
      cSolicitud.signal()
      // Espero a que lo prepare
      while (!platoSushi) cPlatoSushi.await()
      platoSushi = false
      log(s"\t\t\t\tCliente $id ya tiene su plato")
      // Le hago la reverencia
      reverencia = true
      cReverencia.signal()
      log(s"\t\t\t\tCliente $id se inclina ante Itamae")
    } finally {
      l.unlock()
    }
  }

  def siguienteCliente()={
    l.lock()
    try {
      itamaeDisponible = true
      cItamaeDisponible.signal()
      log(s"\t\t\t\tItamae disponible")
      // Espero a que me pidan sushi
      while (!solicitudSushi) cSolicitud.await()
      solicitudSushi = false
    } finally {
      l.unlock()
    }
  }
  def itamaeSirveSushi() = {
    l.lock()
    try {
      // Preparo el sushi y aviso al cliente que está listo
      platoSushi = true
      cPlatoSushi.signal()
      // Me espero a que me haga la reverencia el cliente anterior antes de servir al siguiente
      while (!reverencia) cReverencia.await()
      reverencia = false
      log(s"\t\t\t\tCliente servido")
    } finally {
      l.unlock()
    }
  }
}

class Cocinero extends Thread{
  private var fin = false
  override def run = {
    while (!Thread.interrupted() && !fin) {
      try
        SushiBar.siguienteCliente()
        Thread.sleep(Random.nextInt(100)) //Itamae está preparando el pedido
        SushiBar.itamaeSirveSushi()
      catch
        case e: InterruptedException => fin = true
    }
  }
}
object EjemSushi2 {
  def main(args:Array[String]) = {
    val Clientes = 20
    val C = 1
    val cliente = new Array[Thread](Clientes)
    val itamae = new Cocinero
    itamae.start()
    for (i <- cliente.indices) {
      cliente(i) = thread {
        for (j<-0 until C){
          Thread.sleep(Random.nextInt(1000))
          SushiBar.entraCliente(i)
          SushiBar.clientePideSushi(i)
          Thread.sleep(Random.nextInt(500)) //cliente está degustando su sushi
          SushiBar.saleCliente(i)
        }
      }
    }
    cliente.foreach(_.join())
    itamae.interrupt()
    itamae.join()
    log ("Fin del programa")
  }


}
