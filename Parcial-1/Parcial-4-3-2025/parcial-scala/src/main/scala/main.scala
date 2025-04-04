trait ImmutableVector {
  def toList: List[Double]
  def dim: Int
  def +(v: ImmutableVector): ImmutableVector
  def *(v: ImmutableVector): Double
}

class MiVector private (private val elems: List[Double]) extends ImmutableVector {

  def this(values: Double*) = this(values.toList)

  override def toList: List[Double] = elems
  override def dim: Int = elems.length

  override def +(v: ImmutableVector): ImmutableVector = v match
    case that:MiVector =>
      if (this.elems.length != that.elems.length)
        throw new RuntimeException("Vectors dimension must be equal")
      new MiVector(this.elems.zip(that.elems).map { case (a, b) => a + b })

  override def *(v: ImmutableVector): Double = v match
    case that:MiVector =>
      if (this.elems.length != that.elems.length)
        throw new RuntimeException("Vectors dimension must be equal")
      this.elems.zip(that.elems).map { case (a, b) => a * b }.sum

  override def toString: String = elems.mkString("(", ", ", ")")

  override def equals(obj: Any): Boolean = obj match
    case that: MiVector => this.elems == that.elems
    case _              => false

  override def hashCode(): Int = elems.hashCode()
}

object  EjerciciosControl {
  def main(args: Array[String]) = {}
    val v = new MiVector(1, 2, 3)
    val w = new MiVector(2, 3, 4)
    println(s"suma de $v y $w: ${v + w}")
    println(s"producto escalar de $v y $w: ${v * w}")
    println(s"Son iguales $v y $w?: ${w == v}")
    println(s"Son iguales $v y ${new MiVector(1, 2, 3)}?: ${v == new MiVector(1, 2, 3)}")
    try {
      val x = v + new MiVector(1, 2)
    } catch {
      case _ => println("no son de la misma dimensión")
    }
}

def merge[A](lq: (A, A) => Boolean)(l1: List[A], l2: List[A]): List[A] =
  (l1, l2) match
    case (Nil, _) => l2
    case (_, Nil) => l1
    case (h1 :: t1, h2 :: t2) =>
      if (lq(h1, h2)) h1 :: merge(lq)(t1, l2)
      else h2 :: merge(lq)(l1, t2)

def mergeSort[A](lq: (A, A) => Boolean)(lista: List[A]): List[A] = {
  val n = lista.length / 2
  if (lista.length <= 1) lista
  else {
    val (left, right) = lista.splitAt(n)
    merge(lq)(mergeSort(lq)(left), mergeSort(lq)(right))
  }
}

def powerset[A](list: List[A]): List[List[A]] = list match {
  case Nil => List(Nil)
  case head :: tail =>
    val tailSubsets = powerset(tail)
    tailSubsets ++ tailSubsets.map(head :: _)
}

def knapsack(n: Int, list: List[Int]): Option[List[Int]] = {
  powerset(list).find(sublist => sublist.sum == n)
}


