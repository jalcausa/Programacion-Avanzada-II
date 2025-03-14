package practica_22

class EfficientQueue[T] private (private val front: List[T], private val rear: List[T]) extends ImmutableQueue[T] {
  def this(p: T*) =
    this(p.toList, Nil)
    
  def enqueue(elem: T): ImmutableQueue[T] =
    new EfficientQueue(front, elem::rear)

  def dequeue(): (T, ImmutableQueue[T]) =
    if (front.isEmpty)
      if (rear.isEmpty) throw new NoSuchElementException("Empty queue")
      else {
        (rear.reverse.head, new EfficientQueue(rear.reverse.tail, Nil))
      }
    else
      (front.head, new EfficientQueue(front.tail, rear))

  def isEmpty: Boolean =
    front.isEmpty && rear.isEmpty

  override def toString: String =
    (front ++ rear.reverse).mkString("Queue(", ", ", ")")
  
  override def equals(obj: Any): Boolean =
    obj match
      case that: EfficientQueue[_] => front ++ rear.reverse == that.front ++ that.rear.reverse
      case _ => false

  override def hashCode(): Int =
    front.hashCode() + rear.hashCode()
}

@main def testImmutableQueue(): Unit = {
  val squeue = new EfficientQueue[Int]()
  val q = squeue.enqueue(1).enqueue(2).enqueue(3).enqueue(4)
  assert(q.dequeue() == (1, EfficientQueue(2, 3, 4)), s"${q.dequeue()} should be equal to (1, SimpleQueue(List(2, 3, 4)))")
  assert(squeue.isEmpty, s"{q} should be empty")
  assert(!q.isEmpty, s"{q should not be empty")
  val q2 = EfficientQueue(1, 2, 3, 4)
  assert(q == q2, s"${q} and ${q2} should be equal")
  assert(q.hashCode() == q2.hashCode(), s"The hash codes of ${q} and ${q2} should be equal")
}
