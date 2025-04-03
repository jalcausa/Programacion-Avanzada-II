import scala.annotation.tailrec

/*
  1) Utilizando foldRight, define las funciones
  def sum(l:List[Int]):Int
  def product(l:List[Int]):Int
  def length[A](l:List[A]):Int
  que, respectivamente, suman/multiplican los elementos de la lista l,
  y calculan su longitud, respectivamente.
  Ejemplos:
  sum(List(1,2,3)) == 6
  product(List(1,3,5)) == 15
  length(List(“Hola”, “ ”, “Mundo”)) == 3
**/

def sum(l: List[Int]): Int =
  l.foldRight(0)(_+_)

def product(l:List[Int]):Int =
  l.foldRight(1)(_*_)

def length[A](l:List[A]):Int =
  l.foldRight(0)((_, y) => y + 1)

/*
  2) Utilizando foldLeft o foldRight define las funciones
  def reverse[A](l:List[A]):List[A]
  def append[A](l1:List[A],l2:List[A]):List[A]
  que calculan la longitud de la lista l y la invierten. Ejemplos:
  reverse(List(1,2,3)) == List(3,2,1)
  append(List(1,2,3),List(1,2)) == List(1,2,3,1,2)
*/

def reverse[A](l:List[A]):List[A] =
  l.foldLeft(Nil: List[A])((acc, elem) => elem::acc)

def append[A](l1:List[A],l2:List[A]):List[A] =
  l1.foldRight(l2)((elem, l2) => elem::l2)

/*
  3) Utilizando foldLeft o foldRight define la función
  def existe[A](l:List[A],f:A=>Boolean):Boolean
  que comprueba si l tiene un elemento que satisface f. Ejemplos:
  existe(List(1,2,3),_>2) == true
  existe(List(“Hola”,”Mundo”),_.length>=5) == true
  existe(List(“Hola”,”Mundo”),_.length<3) == false
*/

def existe[A](l:List[A],f:A=>Boolean):Boolean =
  l.foldLeft(false)((flag, elem) => flag || f(elem))

/*
  4) Define la función
  def f(l:List[Int]):List[Int]
  que dada la lista l construye una lista con los valores absolutos de los elementos negativos de l. Por
  ejemplo,
  f(List(1,-2,3,-4,-5,6)) == List(2,4,5)
  Implementa la función de dos formas
  a) Mediante una función recursiva de cola, haciendo uso del pattern matching
  b) Usando únicamente funciones de orden superior (map, filter, etc.)
* */
// a):
def f1(l: List[Int]): List[Int] =
  @tailrec
  def bucle(l: List[Int], res: List[Int]): List[Int] =
    l match
      case Nil => res
      case x::xs =>
        if (x >= 0) bucle(xs, res)
        else bucle(xs, -x::res)
  bucle(l, Nil).reverse

// b):
def f2(l: List[Int]): List[Int] =
  l.filter(_<0).map(-_)

/*
  5) Usando foldRight implementa la función
  def unzip[A](l:List[(A,B)]):(List[A],List[B])
  que dada una lista de tuplas List((a1, b1), …, (an, bn)) devuelve dos listas de la forma List(a1,
  …, an) y List(b1, …, bn). Ejemplo:
  unzip(List((1,’a’),(2,’b’),(3,’c’))) == (List(1,2,3), List(‘a’,‘b’,‘c’))
* */

def unzip[A, B](l:List[(A,B)]):(List[A],List[B]) =
  def aux(elem: (A,B), acc:(List[A], List[B])): (List[A], List[B]) =
    (elem._1 :: acc._1, elem._2 :: acc._2)
  l.foldRight((Nil: List[A], Nil: List[B]))(aux)