import scala.::
import scala.annotation.tailrec
// Ejercicio 1

def sum(l: List[Int]): Int = {
  l.foldRight(0)(_+_)
}

def product(l: List[Int]): Int = {
  l.foldRight(1)(_*_)
}

def length[A](l: List[A]): Int = {
  l.foldRight(0)((elem, acc) => acc + 1)
}

// Ejercicio 2
// Esto no invierte la lista, devuelve la misma
//def reverse[A] (l: List[A]): List[A] = {
//  l.foldRight(List[A]())((elem, acc) => (elem::acc))
//}

def reverse[A] (l: List[A]): List[A] = {
  l.foldLeft(List[A]())((acc, elem) => elem::acc)
}

def append[A](l1: List[A], l2: List[A]): List[A] = {
  l1.foldRight(l2)((elem, acc) => elem::acc)
}

// Ejercicio 3
def existe[A](l:List[A],f:A=>Boolean): Boolean = {
  l.foldLeft(false)((acc, elem) => acc || f(elem))
}

// Ejercicio 4
// Version recursiva de cola
def f(l:List[Int]):List[Int] = {
  @tailrec
  def bucle(acc: List[Int], rest: List[Int]): List[Int] = {
    rest match {
      case Nil => acc
      case x::xs =>
        if x >= 0 then bucle(acc, xs)
        else bucle((-x)::acc, xs)
    }
  }
  bucle(Nil, l).reverse
}

// Usando funciones de orden superior
def f2(l:List[Int]):List[Int] = {
  l.filter(_<0).map(-_)
}

// Ejercicio 5
def unzip[A, B](l:List[(A,B)]):(List[A],List[B]) = {
  l.foldRight((List[A](), List[B]())) {case ((x,y), (l1, l2)) => (x::l1, y::l2)}
}

// Ejercicio 6
def compose[A](lf:List[A=>A],v:A):A = {
  lf.foldRight(v)((elem, acc) => elem(acc))
}

// Ejercicio 7
def remdups[A](lista:List[A]):List[A] = {
  lista.foldRight(List[A]()) { (elem, acc) => {
      acc match
        case Nil => elem :: acc
        case x::xs =>
          if x == elem then acc
          else elem :: acc
    }
  }
}
/*
Sin pattern matching:
def remdups[A](lista:List[A]):List[A] =
  def aux(a: A, acc: List[A]): List[A] =
    if (acc.isEmpty) List(a)
    else if (acc.head == a) acc
    else a::acc
  lista.foldRight(Nil)(aux)
*/

// Ejercicio 8
def fibonnaci(n:Int):Int = {
  val l = List.fill(n)(0)
  val fib = l.foldRight(List[Int]()){(elem, acc) =>
    acc match {
      case Nil => 1 :: acc
      case x::y::xs => (x + y) :: acc
      case x::xs => x::acc
    }
  }
  fib.head
}

// Ejercicio 9
def inits[A](l:List[A]): List[List[A]] = {
  // Hacemos :: de la lista vacía porque tenemos una lista de listas es como si tuviesemos
  // List()::List(1)::List(1,2)::List(1,2,3) y añadir List() al principio es crucial
  // para construir los nuevos prefijos y mantener los intermedios hasta llegar a la lista completa
  l.foldRight(List(List[A]())){(elem, acc) => List[A]() :: acc.map(lista => elem::lista)}
}

// Ejercicio 10
def halfEven(l1:List[Int],l2:List[Int]):List[Int] = {
  @tailrec
  def bucle(acc: List[Int], rest1: List[Int], rest2: List[Int]): List[Int] = {
    (rest1, rest2) match {
      case (Nil, Nil) => acc
      case (Nil, y::ys) =>
        if (y % 2 == 0) then bucle((y/2)::acc, Nil, ys)
        else bucle(acc, Nil, ys)
      case (x::xs, Nil) =>
        if (x % 2 == 0) then bucle((x/2)::acc, xs, Nil)
        else bucle(acc, xs, Nil)
      case (x::xs, y::ys) =>
        if ((x + y) % 2 == 0) then bucle((x + y) / 2 :: acc, xs, ys)
        else bucle(acc, xs, ys)
    }
  }
  bucle(Nil, l1, l2)
}

def halfEven2(l1:List[Int],l2:List[Int]):List[Int] = {
  l1.zip(l2).filter(elem => (elem._1 + elem._2) % 2 == 0).map((a,b) => (a+b)/2)
}

// Ejercicio 11: usar split y groupBy para agrupar todos los que son iguales
// y así tengo por un lado los ERROR, los INFO, etc
// Parte (1)

def countMessages(l: List[String]): Map[String, Int] = {
  l.map(s => s.split(":")).groupBy(arr => arr(0)).map((key, value) => (key, value.length))
}

def extractMessages(l: List[String]): List[String] = {
  l.map(s => s.split(":")).filter(arr => arr(0) == "ERROR").map(arr => s"${arr(0)}: ${arr(1)}")
}