package concurrencia

import java.util.concurrent.locks.ReentrantLock
import java.util.Date

// Package object con la función thread
package object concurrencia {
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run = body
    }
    t.start()
    t
  }
}

// Gestor corregido
object gestorBlocks {
  private var nLectores = 0
  private var escribiendo = false
  private var nEscritores = 0
  private val l = new ReentrantLock(true)
  private val okLeer = l.newCondition()
  private val okEscribir = l.newCondition()

  def entraLector(id: Int) = {
    l.lock()
    try {
      // La primera condición es para ver esperar si hay algún escritor dentro en ese momento
      // y la segunda es por si hay alguno esperando ya no dejar entrar al lector ya que tienen preferencia
      // los escritores
      while (escribiendo || nEscritores > 0) okLeer.await()
      nLectores += 1
      log(s"Lector $id entra en la BD")
    } finally {
      l.unlock()
    }
  }

  def entraEscritor(id: Int) = {
    l.lock()
    try {
      nEscritores += 1
      while (escribiendo || nLectores > 0) okEscribir.await()
      escribiendo = true
      log(s"Escritor $id entra en la BD")
    } finally {
      l.unlock()
    }
  }

  def saleLector(id: Int) = {
    l.lock()
    try {
      nLectores -= 1
      if (nLectores == 0) okEscribir.signal()
      log(s"Lector $id sale de la BD")
    } finally {
      l.unlock()
    }
  }

  def saleEscritor(id: Int) = {
    l.lock()
    try {
      nEscritores -= 1
      escribiendo = false
      if (nEscritores > 0) okEscribir.signal()
      else okLeer.signalAll()
      log(s"Escritor $id sale de la BD")
    } finally {
      l.unlock()
    }
  }
}

// Programa principal de prueba
object MainPrueba extends App {
  import concurrencia._

  println("=== Iniciando prueba del Gestor de Lectores-Escritores ===")

  // Función para simular trabajo de lectura
  def trabajoLectura(id: Int, tiempo: Int) = {
    gestorBlocks.entraLector(id)
    Thread.sleep(tiempo)  // Simula tiempo de lectura
    gestorBlocks.saleLector(id)
  }

  // Función para simular trabajo de escritura
  def trabajoEscritura(id: Int, tiempo: Int) = {
    gestorBlocks.entraEscritor(id)
    Thread.sleep(tiempo)  // Simula tiempo de escritura
    gestorBlocks.saleEscritor(id)
  }

  // Crear varios lectores
  val lectores = for (i <- 1 to 3) yield {
    thread {
      for (j <- 1 to 2) {
        trabajoLectura(i, 1000 + scala.util.Random.nextInt(1000))
        Thread.sleep(500) // Pausa entre accesos
      }
    }
  }

  // Crear varios escritores
  val escritores = for (i <- 1 to 2) yield {
    thread {
      for (j <- 1 to 2) {
        trabajoEscritura(i, 1500 + scala.util.Random.nextInt(1000))
        Thread.sleep(800) // Pausa entre accesos
      }
    }
  }

  // Esperar a que terminen todos los hilos
  (lectores ++ escritores).foreach(_.join())

  println("=== Prueba completada ===")
}