import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.Map

/*
1) Escribe una función recursiva de cola primeFactors(n: Int): List[Int] que devuelva una
lista con los factores primos de un entero positivo dado n. Ejemplos:
  println(primeFactors(60)) // Output: List(2, 2, 3, 5)
  println(primeFactors(97)) // Output: List(97)
  println(primeFactors(84)) // Output: List(2, 2, 3, 7)
 */

def primeFactors (n: Int): List[Int] = {
  @tailrec
  def bucle(n:Int, d: Int, acc: List[Int]): List[Int] =
    if (n == 1) acc
    else if (n % d == 0) bucle (n/d, d, d::acc)
    else bucle (n, d + 1, acc)
  bucle(n, 2, Nil).reverse
}

/*
  Si en vez de usar acc :+div usamos div:acc y al final hacemos reverse
  es más eficiente
*/

/*
  2) Escribe una función recursiva de cola binarySearch(arr: Array[Int], elt: Int):
  Option[Int] que devuelva el índice de elt (Some(i)) en un array ordenado utilizando el
  algoritmo de búsqueda binaria, o None en caso de que el elemento no se esté. Ejemplos:
  val arr = Array(1, 3, 5, 7, 9, 11)
  println(binarySearch(arr, 5)) // Output: Some(2)
  println(binarySearch(arr, 10)) // Output: None
*/

def binarySearch(arr: Array[Int], elem: Int): Option[Int] = {
  @tailrec
  def bucle(i: Int, j: Int): Option[Int] = {
    if (i > j) None
    else
      val m = (i + j) / 2
      if (arr(m) == elem) Some(m)
      else if (arr(m) > elem) bucle(i, m-1)
      else bucle(m + 1, j)
  }
  bucle(0, arr.length - 1)
}

/*
  3) Define una función recursiva genérica unzip que tome una lista de tuplas con dos componentes y
  que devuelva una tupla con dos listas: una con las primeras componentes y otra con las segundas.
  Por ejemplo,
  unzip(List((10, 'a'), (20, 'b'), (10, 'c'))
  == (List(10, 20, 30), List('a', 'b', 'c'))
*/

// Versión recursiva normal
def unzip1[A, B] (l: List[(A, B)]): (List[A], List[B]) = {
  l match {
    case Nil => (Nil, Nil)
    case (a,b) :: tail =>
      val l = unzip1(tail)
      (a::l._1, b::l._2)
  }
}

// Versión recursiva de cola
def unzip2[A, B] (l: List[(A, B)]): (List[A], List[B]) = {
  @tailrec
  def bucle(lista: List[(A, B)], accA: List[A], accB: List[B]): (List[A], List[B]) = {
    lista match {
      case Nil => (accA, accB)
      case (a, b)::tail => bucle(tail, a::accA, b::accB)
    }
  }
  val res = bucle(l, Nil, Nil)
  (res._1.reverse, res._2.reverse)
}
/*
Versión usando foldL de ChatGPT:
def unzip[A, B](l: List[(A,B)]): (List[A], List[B]) =
  l.foldLeft((List[A](), List[B]())) { case ((accA, accB), (a, b)) =>
    (a :: accA, b :: accB)
  } match {
    case (listA, listB) => (listA.reverse, listB.reverse)
  }
*/

/*
  4) Define una función recursiva genérica zip que tome dos listas y devuelva una lista de tuplas, donde
    las primeras componentes se tomen de la primera lista y las segundas componentes de la segunda
    lista. Por ejemplo: zip(List(10, 20, 30), List('a', 'b', 'c')) == List((10, 'a'), (20, 'b'), (10, 'c'))
    zip(List(10, 20, 30), List('a', 'b')) == List((10,'a'), (20,'b'))
*/

def zip[A, B] (listA: List[A], listB: List[B]): List[(A, B)] = {
  listA match {
    case Nil => Nil
    case a::tail => (a, listB.head) :: zip(tail, listB.tail)
  }
}

/*
  5) Implementa una operación filter(l, f) que tome una lista l de elementos de tipo A y una función f: A
  => Boolean y que devuelva una lista con los elementos e de l que satisfacen f(e). Por ejemplo:
  println(filter(List(1,2,3,4,5), _ % 2 == 0)) // Output: List(2,4)
*/

def filter[A] (l: List[A], f: A => Boolean): List[A] = {
  @tailrec
  def bucle(lista: List[A], acc:List[A]): List[A] = {
    lista match {
      case Nil => acc
      case a::rest =>
        if (f(a)) then bucle(rest, a::acc)
        else bucle(rest, acc)
    }
  }
  bucle(l, Nil).reverse
}

/*
  6) Implementa una operación map(l, f) que tome como argumentos una lista l de elementos de tipo
  A y una función f: A => B y que devuelva una lista de elementos de tipo B con los elementos
  resultantes de aplicar f a cada uno de los elementos de l.
  println(map(List(1,2,3,4,5), _ * 2)) // Output: List(2,4,6,8,10)
*/

def map[A, B] (l: List[A], f: A => B): List[B] = {
  @tailrec
  def bucle(lista: List[A], acc: List[B]): List[B] = {
    lista match {
      case Nil => acc
      case a::rest => bucle(rest, f(a)::acc)
    }
  }
  bucle(l, Nil).reverse
}

/*
  7) Implementa una operación groupBy(l, f) que tome como argumentos una lista l de elementos de
  tipo A y una función f: A => B y que devuelva un objeto de tipo Map[B, List[A]] que asocie una lista
  con los elementos e de l con el mismo f(e).
  println(groupBy(List(1,2,3,4,5), _ % 2 == 0))
  // Output: Map(false -> List(5, 3, 1), true -> List(4, 2))
*/

def groupBy[A, B] (l: List[A], f: A => B): mutable.Map[B, List[A]] = {
  @tailrec
  def bucle(lista: List[A], acc: mutable.Map[B, List[A]]): mutable.Map[B, List[A]] = {
    lista match {
      case Nil => acc
      case a::rest =>
        if (acc.contains(f(a))) then
          var imagen = acc(f(a))
          bucle(rest, acc.addOne(f(a), a::imagen))
        else
          bucle(rest, acc.addOne(f(a), List(a)))
    }
  }
  bucle(l, mutable.Map())
}

/*
  8) Implementa una operación reduce(l, f) que toma como argumentos una lista l de elementos de tipo
  A y una función f de tipo (A, A) => A y que devuelva el resultado de combinar todos los elementos
  de l utilizando la función f. Por ejemplo: println(reduce(List(1,2,3,4,5), _ + _)) // Output: 15
*/

// Versión recursiva de cola
def reduce[A] (l: List[A], f: (A, A) => A): A = {
  @tailrec
  def bucle(acc: A, lista: List[A]): A = {
    lista match {
      case Nil => acc
      case x::xs => bucle(f(acc, x), xs)
    }
  }
  bucle(l.head, l.tail)
}

// Versión recursiva genérica
def reduce2[A] (l: List[A], f: (A, A) => A): A = {
  l match {
    case Nil => throw new RuntimeException("Empty list not supported")
    case x::Nil => x
    case x::xs => f(x, reduce2(xs, f))
  }
}

/*
  9) Implementa una función recursiva para generar todos los subconjuntos de un conjunto
  determinado. Conviértela en recursiva de cola.
  println(subsets(Set())) // Output: Set(Set())
  println(subsets(Set(1))) // Output: Set(Set(), Set(1))
  println(subsets(Set(1,2))) // Output: Set(Set(),Set(1),Set(2),Set(1,2))
  println(subsets(Set(1, 2, 3)))
  // Output: Set(Set(),Set(1),Set(2),Set(1,2),Set(3),Set(1,3),Set(2,3),Set(1,2,3))
*/

// Versión recursiva de cola
def subsets[A] (conj: Set[A]): Set[Set[A]] = {
  @tailrec
  def bucle(acc: Set[Set[A]], left: Set[A]): Set[Set[A]] = {
    if left.isEmpty then acc
    else
        val elem = left.head
        val newSets = acc.map(_+elem)
        bucle(acc ++ newSets, left-elem)
  }
  bucle(Set(Set()), conj)
}
