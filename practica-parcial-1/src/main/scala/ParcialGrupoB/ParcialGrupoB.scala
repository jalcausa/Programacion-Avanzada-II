import scala.annotation.tailrec

trait ImmutableSet[T] {
  def add(elem: T): ImmutableSet[T] // añade el elemento elem al conjunto si no está presente
  def remove(elem: T): ImmutableSet[T] // elimina elem del conjunto; no modifica el conjunto si no está
  def contains(elem: T): Boolean // comprueba si un elemento está en el conjunto
  def size: Int // devuelve el número de elementos en el conjunto
  def isEmpty: Boolean // comprueba si el conjunto está vacío
}

class SimpleSet[T] private (val elems: List[T]) extends ImmutableSet[T] {

  def this(els: T*) = {
    this(els.toList)
  }

  override def add(elem: T): ImmutableSet[T] = {
    if this.contains(elem) then this
    else new SimpleSet[T](elem::elems)
  }

  override def remove(elem: T): ImmutableSet[T] = {
    if this.contains(elem) then new SimpleSet[T](elems.filter(_!=elem))
    else this
  }

  override def contains(elem: T): Boolean = if this.elems.contains(elem) then true else false

  override def size: Int = elems.length

  override def isEmpty: Boolean = this.elems.isEmpty

  def toList: List[T] = {
    elems
  }

  def union(other: SimpleSet[T]): SimpleSet[T] = {
    @tailrec
    def bucle(acc: List[T], rest: List[T]): List[T] = {
      rest match {
        case Nil => acc
        case (x::xs) =>
          if acc.contains(x) then bucle(acc, xs)
          else bucle(x::acc, xs)
      }
    }
    new SimpleSet[T](bucle(this.elems, other.elems))
  }

  def intersection(other: SimpleSet[T]): SimpleSet[T] = {
    new SimpleSet[T](this.elems.filter(other.elems.contains(_)))
  }

  def difference(other: SimpleSet[T]): SimpleSet[T] = {
    new SimpleSet[T](this.elems.foldLeft(List[T]()){(acc, elem) =>
      if other.elems.contains(elem) then acc
      else elem::acc
    })
  }

  override def toString: String = {
    elems.mkString("Set(", ",", ")")
  }

  override def hashCode(): Int = {
    this.elems.foldRight(0)((elem, acc) => acc + elem.hashCode())
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: SimpleSet[_] =>
        this.elems.foldRight(true)((elem, acc) => acc && that.elems.contains(elem))
      case _ => false
    }
  }

}

def palabrasSignificativas(sentences: List[String], stopWords: Set[String]): Map[String, Int] = {
  sentences
    .map(s => s.split(":"))
      .filter(arr => arr(0).toUpperCase == "FINAL")
        .map(arr => arr(1).trim.toLowerCase())
          .flatMap(s => s.split(" "))
            .filter(s => !stopWords.contains(s))
              .groupBy(identity)
                .map((x,y) => (x, y.length))
}

object  ParcialGrupoB {
  def main(args: Array[String]) = {}
  val v = new SimpleSet[Int](1, 2, 3, 6)
  val w = new SimpleSet[Int](3, 4, 5, 6)
  val s = new SimpleSet[Int](6, 1, 3, 2)
  println(s"El conjunto $v contiene 1? ${v.contains(1)}")
  println(s"El conjunto $v contiene 7? ${v.contains(7)}")
  println(s"Add 10 al conjunto $v da como resultado: ${v.add(10)}")
  println(s"Add 1 al conjunto $v da como resultado: ${v.add(1)}")
  println(s"Remove 1 al conjunto $v da como resultado: ${v.remove(1)}")
  println(s"Remove 10 al conjunto $v da como resultado: ${v.remove(10)}")
  val union = v.union(w)
  println(s"Unión de s1: $v y $w es: $union")
  println(s"Intersección de s1: $v y de s2 $w es: ${v.intersection(w)}")
  println(s"Diferencia de s1: $v y de s2 $w es: ${v.difference(w)}")
  println(s"Son iguales s1: $v y s2: $w? : ${v.equals(w)}")
}