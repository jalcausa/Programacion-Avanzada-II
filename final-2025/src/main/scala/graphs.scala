// Ejercicio 1

// Supongamos el siguiente trait con las operaciones básicas de grafos dirigidos.

trait Graph[N] {
  def addNode(node: N): Graph[N] // añade un nodo al grafo
  def addEdge(from: N, to: N): Graph[N] // añade una arista del nodo from al nodo to, lanza una
  // IllegalArgumentException si alguno de los nodos no existe en el grafo.
  def neighbors(node: N): Set[N] // devuelve los nodos vecinos de un nodo dado (extremos de aristas
  // con el nodo node como origen)
  def hasNode(node: N): Boolean // comprueba si el grafo contiene un nodo
}

// Proporciona una clase GraphOverMap[T] que implemente Graph[T]. La clase debe usar una
// Map[N, Set[N]] para almacenar los nodos y aristas de un grafo. Si un grafo tiene una arista
// a -> b, el map correspondiente tendrá a como clave y b estará en el conjunto de elementos
// asociados a a. Además de los métodos del trait, la clase GraphOverMap[T] proporcionará:
// •	Un constructor principal privado que tome un argumento de tipo Map[N, Set[N]].
// •	Un constructor sin argumentos (this()).
// •	Redefiniciones de los métodos toString (mostrando el conjunto con el formato "Graph(1 -> 2,
// 2 -> 3, 3 -> 1, 3 -> 2)"), hashCode y equals para asegurar que dos maps con los mismos nodos y
// aristas se consideren iguales.

class GraphOverMap[T] private (val map: Map[T, Set[T]])  extends Graph[T] {
  def this() = {
    this(Map[T, Set[T]]())
  }

  override def addNode(node: T): Graph[T] = {
    new GraphOverMap[T](this.map + (node->Set[T]()))
  }

  override def addEdge(from: T, to: T): Graph[T] = {
    if (!this.map.contains(from) || !this.map.contains(to)) throw new IllegalArgumentException("Alguno de los nodos no está")
    else new GraphOverMap[T](this.map + (from -> Set(to)))
  }

  override def neighbors(node: T): Set[T] = {
    this.map.getOrElse(node, Set[T]())
  }

  override def hasNode(node: T): Boolean = {
    this.map.keys.toList.contains(node)
  }

  override def toString: String = {
    this.map.mkString("Graph(", ",", ")")
  }

  override def hashCode(): Int = {
    this.map.keys.foldRight(0)((elem, acc) => acc + elem.hashCode()) +
      this.map.values.foldRight(0)((elem, acc) => acc + elem.hashCode())
  }

// No me ha dado tiempo a acabarlo
//  override def equals(obj: Any): Boolean = {
//    obj match
//      case that: GraphOverMap[T] =>
//        this.map.keys.foldRight(true)((elem, acc) => acc && elem.equals()) &&
//          this.map.values.foldRight(true)((elem, acc) => acc + elem.equals(obj))
//      case _ => false
//  }
}