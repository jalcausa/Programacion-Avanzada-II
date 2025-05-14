import java.util.concurrent.Semaphore

class SupermercadoSemaforos extends Supermercado {

  private val permanente: Cajero = new Cajero(this, true) // crea el primer cajero, el permanente
  permanente.start()

  // TODO: Definir los semáforos y variables necesarios

  override def fin(): Unit = {
    // TODO: Implementar
  }

  override def nuevoCliente(id: Int): Unit = {
    // TODO: Implementar
  }

  override def permanenteAtiendeCliente(id: Int): Boolean = {
    // TODO: Implementar

    true // borrar
  }

  override def ocasionalAtiendeCliente(id: Int): Boolean = {
    // TODO: Implementar

    true // borrar
  }
}