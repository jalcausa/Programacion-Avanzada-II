class Ejemplos {
  /* Listas
  LA diferencia con los arrays es que las listas son inmutables
  */
  val res1 = List(1,2,3)
  val res2 = List(4,5,6)
  // Para concatenar listas:
  val res3 = res1:::res2
  /*
  -Añadir un elemento a la cabeza:
  0::res1
  -Crear una lista concatenando elementos:
  1::4::5::Nil
  -Acceder a la posición 1 de la lista:
  lista.apply(1)
  -Drop crea una nueva lista eliminando
   el número de elementos que le indiques en la cabeza
  -Funcion exists: devuelve un Boolean si algún elemento de la lista verifica la condición
  l.exists((x:Int) => x%2 == 0)
  l.exists((x:Int) => x>3) // Se puede escribir así también:
  l.exists(_>3)
   */
}
