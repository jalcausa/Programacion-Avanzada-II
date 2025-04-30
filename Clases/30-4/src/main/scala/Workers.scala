import java.util.concurrent.Semaphore

/*
Supongamos que tenemos n + 1 workers y cada uno tiene su bucle en el que están
haciendo su trabajo. Cada uno de estos bucles son asíncronos pero deben sincronizarse
al final de sus iteraciones.
Necesitamos que el worker i no puede iniciar la iteración iter + 1 hasta que todos
los worker han terminado la iteración iter = 0, 1, 2, ...

Si hay n + 1 worker los n primeros se comportan igual pero el n + 1 que es el último
tiene que ser el que despierte al resto.

Necesitamos controlar el número de iteraciones para que no puedan entrar nuevos
trabajadores hasta que no hayan salido todos los que había en la anterior iteración
 */

object Barrera {
  private val N = 5
  private var numT = 0 // necesito un mutex para esta variable
  private val mutex = new Semaphore(1)
  private val espera = new Semaphore(0)
  private val puerta = new Semaphore(1)
  def heTerminado(id: Int, iter: Int): Unit = {
    // El worker id ha terminado la iteración iter
    puerta.acquire()
    mutex.acquire()
    numT += 1
    if (numT < N)
      mutex.release()
      puerta.release()
      espera.acquire()
      mutex.acquire()
    numT -= 1
    if (numT > 0) // Si no es el último en irse despierta al siguiente
      espera.release()
    else
      puerta.release()
    mutex.release()
  }
}