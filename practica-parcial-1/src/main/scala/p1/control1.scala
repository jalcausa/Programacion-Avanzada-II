import scala.annotation.{MainAnnotation, tailrec}

trait ImmutableVector{
  def toList:List[Double] //devuelve una lista con los elementos del vector
  def dim:Int // devuelve la dimensión del vector def
  def +(v:ImmutableVector):ImmutableVector //suma el vector this y v def
  def *(v:ImmutableVector):Double //calcula el producto escalar de this y v
}

class MiVector private (private val elems: List[Double]) extends ImmutableVector {

  def this(els: Double*) = {
    this(els.toList)
  }

  override def toList: List[Double] = elems

  override def dim: Int = elems.length

  override def +(v: ImmutableVector): ImmutableVector = {
    v match {
      case that: MiVector => new MiVector(that.elems.zip(this.elems).map((x,y) => x+y))
      case _ => throw new RuntimeException("No se puede sumar un vector con algo que no sea un vector")
    }
  }

  override def *(v: ImmutableVector): Double = {
    v match {
      case that: MiVector => that.elems.zip(this.elems).map((x,y) => x*y).sum
      case _ => throw new RuntimeException("No se puede hacer el producto escalar de un vector con algo que no sea un vector")
    }
  }

  override def toString: String = {
    elems.mkString("(", ",", ")")
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: MiVector => this.elems == that.elems
      case _ => false
    }
  }

  override def hashCode(): Int = {
    elems.hashCode()
  }
}

def propercuts[A](list:List[A]):List[(List[A],List[A])] = {
  @tailrec
  def bucle(acc: List[(List[A],List[A])], i: Int): List[(List[A],List[A])] = {
    if i == list.length then acc
    else bucle(list.splitAt(i) :: acc, i + 1)
  }
  bucle(List(), 1)
}

// Versión 1, usando sortWith
//def merge[A](lq:(A,A)=>Boolean)(l1:List[A],l2:List[A]):List[A] = {
//  (l1 ++ l2).sortWith(lq)
//}

def merge[A](lq:(A,A)=>Boolean)(l1:List[A],l2:List[A]):List[A] = {
  (l1, l2) match {
    case (Nil, _) => l2
    case (_, Nil) => l1
    case (x::xs, y::ys) =>
      if lq(x,y) then x::merge(lq)(xs, l2)
      else y::merge(lq)(l1, ys)
  }
}

def mergeSort[A](lq:(A,A)=>Boolean)(l: List[A]): List[A] = {
  val i = l.length / 2
  if (l.length <= 1) l
  else
    val (left, right) = l.splitAt(i)
    merge(lq)(mergeSort(lq)(left), mergeSort(lq)(right))
}

def powerset[A](list:List[A]):List[List[A]] = {
  list.foldRight(List(List[A]()))((elem, acc) => acc ++ acc.map(lista => elem::lista))
}

def knapsack(n:Int,list:List[Int]):Option[List[Int]] = {
  val mochila = powerset(list).filter(_.sum==n)
  mochila match {
    case Nil => None
    case _ => Some(mochila.head)
  }
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