package concurrencia

import java.util.concurrent.*
import scala.util.Random
object aseo{
  // CS-Cliente: Esperan si está el Equipo de Limpieza en el aseo
  // CS-EquipoLimpieza: Espera si hay clientes en el aseo

  private var numClientes = 0
  private val mutex = new Semaphore(1)
  private val limpiando = new Semaphore(1)

  def entraCliente(id:Int)={
    mutex.acquire()
    numClientes += 1
    if (numClientes == 1)
      limpiando.acquire()
    log(s"Entra cliente $id. Hay $numClientes clientes.")
    mutex.release()

  }
  def saleCliente(id:Int)={
    mutex.acquire()
    numClientes -= 1
    log(s"Sale cliente $id. Hay $numClientes clientes.")
    if (numClientes == 0)
      limpiando.release()
    mutex.release()
  }
  def entraEquipoLimpieza ={
    limpiando.acquire()
    log(s"        Entra el equipo de limpieza.")
  }
  def saleEquipoLimpieza = {
    log(s"        Sale el equipo de limpieza.")
    limpiando.release()
  }
}

object Ejercicio3 {
  def main(args: Array[String]) = {
    val cliente = new Array[Thread](10)
    for (i <- 0 until cliente.length)
      cliente(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(500))
          aseo.entraCliente(i)
          Thread.sleep(Random.nextInt(50))
          aseo.saleCliente(i)
      }
    val equipoLimpieza = thread {
      while (true)
        Thread.sleep(Random.nextInt(500))
        aseo.entraEquipoLimpieza
        Thread.sleep(Random.nextInt(100))
        aseo.saleEquipoLimpieza
    }
  }
}
