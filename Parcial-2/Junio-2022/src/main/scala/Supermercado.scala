trait Supermercado {
  def fin(): Unit
  def nuevoCliente(id: Int): Unit
  def permanenteAtiendeCliente(id: Int): Boolean
  def ocasionalAtiendeCliente(id: Int): Boolean
}