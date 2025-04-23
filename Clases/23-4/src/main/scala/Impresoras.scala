import java.util.concurrent.Semaphore
import scala.util.Random

object Impresoras {
  private var numImp = 2 // Número de impresoras disponibles
  private val espera = new Semaphore(1) // Lo ponemos a 1 para usar semáforos binarios
  private val mutex = new Semaphore(1) // mutex sobre numImp

  // Siempre debemos asegurar que al salir de este method tenga una impresora y esperarme hasta
  // que la tenga
  def quieroImpresora(id: Int) = {
    // La diferencia entre un acquire y un while es que el while es ESPERA ACTIVA, sigue en el
    // procesador mientras que el acquire es ESPERA BLOQUEANTE, sale del procesador el proceso
    espera.acquire()
    mutex.acquire()
    numImp -= 1
    if (numImp > 0)
      espera.release()
      log(s"Usuario $id  coge una impresora. Hay: $numImp impresoras libres")
    mutex.release()
  }

  def devuelvoImpresora(id: Int) = {
    mutex.acquire()
    numImp += 1
    if (numImp == 1)
      espera.release()
    log(s"Usuario $id  devuelve una impresora. Hay: $numImp impresoras libres")
    mutex.release()
  }

  def main(args:Array[String]): Unit =
    val usuario = new Array[Thread](10)
    for (i <- 0 until usuario.length)
      usuario(i) =  thread {
        for (j<-0 until 3)
          quieroImpresora(i)
          Thread.sleep(Random.nextInt(200))
          devuelvoImpresora(i)
      }
}

// Condición de sincronización: usuario i espera hasta que haya impresoras disponibles