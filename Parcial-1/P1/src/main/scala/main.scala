import scala.annotation.tailrec

/*
1) Escribe una función recursiva de cola primeFactors(n: Int): List[Int] que devuelva una
lista con los factores primos de un entero positivo dado n. Ejemplos:
println(primeFactors(60)) // Output: List(2, 2, 3, 5)
println(primeFactors(97)) // Output: List(97)
println(primeFactors(84)) // Output: List(2, 2, 3, 7)
 */

def primeFactors (n: Int): List[Int] =
  @tailrec
  def aux(n: Int, i: Int, res: List[Int]): List[Int] =
    if (n == 1) res
    else if (n % i != 0) aux(n, i + 1, res)
    else aux(n / i, i, i::res)
  aux(n, 2, Nil).reverse

/*
  2) Escribe una función recursiva de cola binarySearch(arr: Array[Int], elt: Int):
  Option[Int] que devuelva el índice de elt (Some(i)) en un array ordenado utilizando el
  algoritmo de búsqueda binaria, o None en caso de que el elemento no se esté. Ejemplos:
  val arr = Array(1, 3, 5, 7, 9, 11)
  println(binarySearch(arr, 5)) // Output: Some(2)
  println(binarySearch(arr, 10)) // Output: None
*/

def binarySearch(arr: Array[Int], elt: Int): Option[Int] =
  @tailrec
  def aux(i: Int, j: Int): Option[Int] =
    if (i > j) None
    else
      val piv = i + (j - i) / 2
      if (arr(piv) == elt) Some(piv)
      else if (elt < arr(piv)) aux(i, piv - 1)
      else aux(piv + 1, j)
  aux(0, arr.length - 1)

/*
  3) Define una función recursiva genérica unzip que tome una lista de tuplas con dos componentes y
  que devuelva una tupla con dos listas: una con las primeras componentes y otra con las segundas.
  Por ejemplo,
  unzip(List((10, 'a'), (20, 'b'), (10, 'c'))
  == (List(10, 20, 30), List('a', 'b', 'c'))
*/
// Versión recursiva genérica
def unzip1[A,B](lista: List[(A, B)]): (List[A], List[B]) =
  lista match
    case Nil => (Nil, Nil)
    case (a, b)::tail =>
      val l = unzip1(tail)
      (a::l._1, b::l._2)

def unzip2[A,B](lista: List[(A, B)]): (List[A], List[B]) =
  @tailrec
  def aux(lista: List[(A, B)], listaA: List[A], listaB: List[B]) : (List[A], List[B]) =
    lista match
      case Nil => (listaA, listaB)
      case (a, b)::tail => aux(tail, a::listaA, b::listaB)
  aux(lista, Nil, Nil)

/*
  4) Define una función recursiva genérica zip que tome dos listas y devuelva una lista de tuplas, donde
  las primeras componentes se tomen de la primera lista y las segundas componentes de la segunda
  lista. Por ejemplo:
  zip(List(10, 20, 30), List('a', 'b', 'c')) == List((10, 'a'), (20, 'b'), (10, 'c'))
  zip(List(10, 20, 30), List('a', 'b')) == List((10,'a'), (20,'b'))
*/

def zip[A, B](listaA: List[A], listaB: List[B]): List[(A,B)] =
  (listaA, listaB) match
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (x::xs, y::ys) => (x,y) :: zip(xs, ys)


/*
  5)Implementa una operación filter(l, f) que tome una lista l de elementos de tipo A y una función
  f: A => Boolean y que devuelva una lista con los elementos e de l que satisfacen f(e). Por ejemplo:
  println(filter(List(1,2,3,4,5), _ % 2 == 0)) // Output: List(2,4)
*/

def filter[A](lista: List[A], f: A => Boolean): List[A] =
  @tailrec
  def bucle(lista: List[A], res: List[A]): List[A] =
    lista match
      case Nil => res
      case x::xs =>
        if f(x) then bucle(xs, x::res)
        else bucle(xs, res)
  bucle(lista, Nil).reverse

/*
  6) Implementa una operación map(l, f) que tome como argumentos una lista l de elementos de tipo
  A y una función f: A => B y que devuelva una lista de elementos de tipo B con los elementos
  resultantes de aplicar f a cada uno de los elementos de l.
  println(map(List(1,2,3,4,5), _ * 2)) // Output: List(2,4,6,8,10)
*/

def map[A, B](lista: List[A], f: A => B): List[B] =
  @tailrec
  def bucle(lista: List[A], res: List[B]): List[B] =
    lista match
      case Nil => res
      case x::xs => bucle(xs, f(x)::res)
  bucle(lista, Nil).reverse

/*
  7) Implementa una operación groupBy(l, f) que tome como argumentos una lista l de elementos de
  tipo A y una función f: A => B y que devuelva un objeto de tipo Map[B, List[A]] que asocie una lista
  con los elementos de de l con el mismo f(e).
  println(groupBy(List(1,2,3,4,5), _ % 2 == 0))
  // Output: Map(false -> List(5, 3, 1), true -> List(4, 2))
*/

/* IMPORTANTE:
  El méto-do updated se usa en mapas inmutables cuando queremos que nos devuelva un nuevo
  mapa actualizado con un nuevo elemmento
*/
def groupBy[A, B](lista: List[A], f: A => B): Map[B, List[A]] =
  @tailrec
  def bucle(lista: List[A], res: Map[B, List[A]]): Map[B, List[A]] =
    lista match
      case Nil => res
      case x::xs => bucle(xs, res.updated(f(x), x::res.getOrElse(f(x), Nil)))
  bucle(lista, Map())