import scala.annotation.tailrec

/*
1) Implementa una función que compruebe si una cadena dada es un palíndromo
(ignorando mayúsculas y minúsculas y espacios).
Nota: utilizar las funciones de la librería para la clase String.
*/
def palindromo(palabra: String): Boolean =
  val palabra_sin_sp: String = palabra.filter(c => c != ' ')
  val palabra_rev_sin_sp: String = palabra.reverse.filter(c => c != ' ')
  palabra_sin_sp == palabra_rev_sin_sp

/*
2) Crea un programa que imprima los primeros n números primos.
*/
def primerosPrimos(n:Int): Unit =
  var i = 2
  var c = 0
  var num = 2
  while (c != n)
    while (i < num && num % i != 0)
      i += 1
    if (i == num)
      print(num.toString ++ " ")
      c += 1
    num += 1
    i = 2

/*
3) Implementa un programa que calcule el máximo común divisor (MCD) y el mínimo común múltiplo (MCM)
de dos números. Nota: el tipo devuelto por la función es una tupla de dos componentes.
*/

def maximoMinimo(n1:Int,n2:Int): (Int,Int) =
  def euclides(a: Int, b: Int): Int =
    if (b == 0) a
    else euclides(b, a % b)
  val MCD = euclides(n1, n2)
  val mcm = Math.abs(n1 * n2) / MCD
  (MCD, mcm)

/*
4) Implementa una función que encuentre el segundo elemento más grande de una lista.
Nota: Utilizar las funciones de la librería de la clase List.
*/

def segundoElemento(l:List[Int]):Int =
  val listaSinMax = l.filter(_ != l.max)
  listaSinMax.max


/*
5) Escribe un programa que elimine todos los duplicados de una lista
(sin usar funciones predefinidas como distinct).
*/
def eliminaDuplicados(l:List[Int]): List[Int] =
  def bucle(original: List[Int], visitados: Set[Int]): List[Int] = original match
    case Nil => Nil
    case head :: tail if visitados.contains(head) => bucle(tail, visitados)
    case head :: tail => head::bucle(tail, visitados + head)
  bucle(l, Set.empty)

/*
6) Implementa una función que gire una lista k posiciones a la derecha (por ejemplo, [1, 2, 3, 4, 5]
rotado en 2 se convierte en [4, 5, 1, 2, 3])
*/

// Versión recursiva de cola, hay otra forma más sencilla de hacerlo
// usando recursión normal y las funciones de listas last e init
def gira(l:List[Int],k:Int):List[Int] =
  def aux (i: Int, rest: List[Int], acc: List[Int]): List[Int] =
    if (i == 0)  rest ++ acc.reverse
    else
      rest match
        case Nil => Nil
        case head :: tail => aux(i - 1, tail, head::acc)
  aux(l.length-k, l, Nil)

/*
7) Implementa un programa que compruebe si dos listas son permutaciones entre sí, sin ordenar las listas.
*/

def esPermutacion(l1: List[Int], l2: List[Int]): Boolean = {
  if (l1.length != l2.length) return false
  def contarElementos(lista: List[Int]): Map[Int, Int] = {
    lista.foldLeft(Map[Int, Int]()) {(contador, elemento) =>
      contador + (elemento -> (contador.getOrElse(elemento, 0) + 1))}
  }
  contarElementos(l1) == contarElementos(l2)
}