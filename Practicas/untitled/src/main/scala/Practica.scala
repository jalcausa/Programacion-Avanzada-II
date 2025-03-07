import scala.annotation.tailrec
import scala.collection.mutable

/*
1) Escribe una función recursiva de cola primeFactors(n: Int): List[Int] que devuelva una
lista con los factores primos de un entero positivo dado n. Ejemplos:
  println(primeFactors(60)) // Output: List(2, 2, 3, 5)
  println(primeFactors(97)) // Output: List(97)
  println(primeFactors(84)) // Output: List(2, 2, 3, 7)
 */

def primeFactors(n: Int): List[Int] =
  @tailrec
  def bucle(n: Int, div: Int, acc: List[Int]): List[Int] =
    if (n == 1) acc
    else if (n % div == 0) bucle(n / div, div, acc :+ div)
    else bucle(n, div + 1, acc)
  bucle(n, 2, Nil)

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

def binarySearch(arr: Array[Int], elem: Int): Option[Int] =
  @tailrec
  def bucle(i: Int, j: Int): Option[Int] =
    // Busca en a[i..j]
    if (i > j) None
    else
      val m = (i + j) / 2
      if (arr(m) == elem) Some(m)
      else if (arr(m) > elem) bucle(i, m-1)
      else bucle(m + 1, j)
  bucle(0, arr.length - 1)

/*
  3) Define una función recursiva genérica unzip que tome una lista de tuplas con dos componentes y
  que devuelva una tupla con dos listas: una con las primeras componentes y otra con las segundas.
  Por ejemplo,
  unzip(List((10, 'a'), (20, 'b'), (10, 'c'))
  == (List(10, 20, 30), List('a', 'b', 'c'))
*/

// Versión recursiva normal
def unzip1[A, B](l: List[(A,B)]): (List[A], List[B]) = l match
  case Nil => (Nil, Nil)
  case (a, b) :: r =>
    val l = unzip1(r)
    (a::l._1, b::l._2)

// Versión recursiva de cola
def unzip2[A, B](l: List[(A,B)]): (List[A], List[B]) =
  @tailrec
  def bucle(lista: List[(A, B)], accA:List[A], accB:List[B]): (List[A], List[B]) = lista match
    case Nil => (accA, accB)
    case (a,b) :: r =>
      bucle(r, a::accA, b::accB)
  bucle(l, List(), List())

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
  7) Implementa una operación groupBy(l, f) que tome como argumentos una lista l de elementos de
  tipo A y una función f: A => B y que devuelva un objeto de tipo Map[B, List[A]] que asocie una lista
  con los elementos e de l con el mismo f(e).
  println(groupBy(List(1,2,3,4,5), _ % 2 == 0))
  // Output: Map(false -> List(5, 3, 1), true -> List(4, 2))
*/

import scala.collection.mutable.Map
def groupBy[A,B](l: List[A], f: A => B): mutable.Map[B, List[A]] =
  val m = mutable.Map[B, List[A]]()
  def bucle(lista: List[A], mapa:mutable.Map[B, List[A]]): mutable.Map[B, List[A]] = lista match
    case Nil => mapa
    case a::r =>
      mapa.get(f(a)) match
        case None => mapa += (f(a)->List(a))
        case Some(l) => mapa += (f(a) -> (a :: l))
      bucle(r, mapa)
  bucle(l, m)