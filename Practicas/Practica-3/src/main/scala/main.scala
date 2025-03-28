import scala.annotation.tailrec
// Ejercicio 1

def sum(l: List[Int]): Int =
  l.foldRight(0)(_+_)

def product(l: List[Int]): Int =
  l.foldRight(1)(_*_)

def length[A](l:List[A]): Int =
  l.foldLeft(0)((x, _) => x + 1)

// Ejercicio 2
/*
def reverse[A](l:List[A]):List[A] =
  l.foldRight(Nil)((x, y) => y:+x)
*/
def reverse[A](l:List[A]):List[A] =
  l.foldLeft(Nil)((x, y) => y::x)

def append[A](l1:List[A],l2:List[A]): List[A] =
  l1.foldRight(l2)((x, y) => x::y)

// Ejercicio 3
def existe[A](l:List[A],f:A=>Boolean): Boolean =
  l.foldLeft(false)((acc, x) => acc || f(x))

// Ejercicio 4
def f(l:List[Int]):List[Int] =
  @tailrec
  def aux(l:List[Int], res: List[Int]): List[Int] =
    l match
      case Nil => res
      case a::r =>
        if (a >= 0) aux(r, res)
        else aux(r, -a::res)
  aux(l, Nil).reverse

def f2(l:List[Int]):List[Int] =
  l.filter(_<0).map(-_)

// Ejercicio 7
/*
Sin pattern matching:
def remdups[A](lista:List[A]):List[A] =
  def aux(a: A, acc: List[A]): List[A] =
    if (acc.isEmpty) List(a)
    else if (acc.head == a) acc
    else a::acc
  lista.foldRight(Nil)(aux)
*/
// Usando pattern matching mejor:
def remdups[A](lista:List[A]):List[A] =
  def aux(a: A, acc: List[A]): List[A] = acc match
    case  Nil => a::Nil
    case b::r =>
      if (a == b) acc
      else a::acc
  lista.foldRight(Nil)(aux)

// Ejercicio 11: usar split y groupBy para agrupar todos los que son iguales
// y así tengo por un lado los ERROR, los INFO, etc.