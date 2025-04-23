/*
Supongamos que tenemos n + 1 workers y cada uno tiene su bucle en el que están
haciendo su trabajo. Cada uno de estos bucles son asíncronos pero deben sincronizarse
al final de sus iteraciones.
Necesitamos que el worker i no puede iniciar la iteración iter + 1 hasta que todos
los worker han terminado la iteración iter = 0, 1, 2, ...

Si hay n + 1 worker los n primeros se comportan igual pero el n + 1 que es el último
tiene que ser el que despierte al resto
 */

object Sinc {
  def heTerminado(id: Int, iter: Int): Unit = {
    // El worker id ha terminado la iteración iter
  }
}