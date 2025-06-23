package Practica2

trait ImmutableQueue[T] {
  def enqueue(elem: T): ImmutableQueue[T]
  def dequeue(): (T, ImmutableQueue[T])
  def isEmpty: Boolean
}

/*
  -El private después del nombre de la clase indica que el constructor primario (el que crearía
  un nuevo SimpleQueue a partir de una Lista es privado y solo se puede usar dentro de la propia
  clase.
  -El constructor que aparece abajo es un constructor auxiliar (que sí es público y permite crear
  una nueva SimpleQueue a partir de un número variable de elementos en vez de una lista.
  -Al poner private val elems estamos diciendo que elems es un atributo de instancia de la clase
  y además que es accesible solo desde dentro de ella (por ser private).  Si no pusieramos val
  entonces elems solo sería un parámetro del constructor y NO un atributo de clase, esa es la
  diferencia clave.
**/
class SimpleQueue[T] private (private val elems: List[T]) extends ImmutableQueue[T] {
  def this(selems: T*) =
    this(selems.toList)
  
  def enqueue(elem: T): ImmutableQueue[T] = null

  def dequeue(): (T, ImmutableQueue[T]) = null
  
  def isEmpty: Boolean = true
  
  override def toString: String = ""
  override def equals(obj: Any): Boolean = true
  override def hashCode(): Int = 0
}

@main def testSimpleQueue(): Unit = {
  val squeue = new SimpleQueue[Int]()
  val q = squeue.enqueue(1).enqueue(2).enqueue(3).enqueue(4)
  assert(q.dequeue() == (1, SimpleQueue(2, 3, 4)), s"${q.dequeue()} should be equal to (1, SimpleQueue(List(2, 3, 4)))")
  assert(squeue.isEmpty, s"{q} should be empty")
  assert(!q.isEmpty, s"{q should not be empty")
  val q2 = SimpleQueue(1, 2, 3, 4)
  assert(q == q2, s"${q} and ${q2} should be equal")
  assert(q.hashCode() == q2.hashCode(), s"The hash codes of ${q} and ${q2} should be equal")
}
