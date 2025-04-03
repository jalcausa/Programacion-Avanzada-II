package practica_2

trait ImmutableQueue[T] {
  def enqueue(elem: T): ImmutableQueue[T]
  def dequeue(): (T, ImmutableQueue[T])
  def isEmpty: Boolean
}

class SimpleQueue[T] private (private val elems: List[T]) extends ImmutableQueue[T] {
  def this(selems: T*) =
    this(selems.toList)

  def enqueue(elem: T): ImmutableQueue[T] =
    new SimpleQueue(elems ++ List(elem))

  def dequeue(): (T, ImmutableQueue[T]) =
    (elems.head, new SimpleQueue(elems.tail))

  def isEmpty: Boolean =
    elems.isEmpty

  override def toString: String =
    elems.mkString("Queue(", ", ", ")")

  // Aquí se usa el match para diferencia el tipo de un dato al tener varias posibilidades
  // Cuando se realiza el match, Scala crea una copia del objeto al que se hace referencia en that
  // Por eso podemos acceder al atributo elems de that  
  override def equals(obj: Any): Boolean =
    obj match
      case that: SimpleQueue[_] => this.elems == that.elems
      case _ => false
      
  override def hashCode(): Int =
    elems.hashCode()
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
