/*
--------------------------- Funciones puras ------------------------------------------
Las funciones puras son las que no tienen efectos laterales como:
-Modificar variables, modificar estructuras de datos, etc
-Lanzar una excepción o detener el programa con un error (a veces puede ser necesario
lazarlas, pero las funciones puras no deben hacerlo)

----------------------- Transparencia referencial -------------------------------------
Una llamada a un función con el mismo valor siempre devuelve el mismo resultado sin
importar desde donde las llame, se pueden sustituir por su resultado.
 */

def factIter(n:Int): BigInt =
  var fact:BigInt = 1
  for (i<-1 to n) fact = fact*i
  fact
/*
La versión recursiva es peor que la iterativa en cuanto a memoria, ya que
cada vez que llamo recursivamente a una función necesito apilar tres variables.
  Tiene ese problema, la podemos arreglar usando una función recursiva de cola
*/
def factRec(n:Int):BigInt =
  require(n >= 0) // Exigimos que el argumento sea positivo
  if (n == 0) 1
  else n * factRec(n - 1)

// Versión recursiva de cola, el compilador la transforma en una versión iterativa
// De esta forma no peta la pila cuando le meto 100000, se transforma en iterativa
def fact(n:Int):Int =
  def bucle(i:Int, acc:Int):Int =
    // fact = i!
    if (i == n) acc
    else bucle(i + 1, acc * (i + 1))
  bucle(0,1)

def fibRec(n: Int): Int =
  if (n == 0 || n == 1) n
  else fibRec(n - 1) + fibRec(n - 2)

def fib(n:Int):Int =
  def bucle(i:Int, ac1:Int, ac2:Int): Int =
    // ac1 = fib(i), ac2 == fib(i-1)
    if (i == n) ac1
    else bucle(i + 1, ac1 + ac2, ac1)
  if (n == 0) 0
  else bucle (1, 1, 0)

/*
---------------------- Función de orden superior ----------------------------------
  Son funciones que toman como parámetros otras funciones
*/

def formatoFact(n:Int):String =
  s"El factorial de $n es ${fact(n)}"

def formatoFib(n:Int):String =
  s"El fibonacci de $n es ${fib(n)}"

def formato(n:Int, nombre:String, f:Int=>Int) =
  s"El $nombre de $n es ${f(n)}"

def buscarInt(v:Array[Int], elem:Int):Int =
  def bucle(i:Int):Int =
    if (i == v.length) -1
    else if (v(i) == elem) i
    else bucle (i + 1)
  bucle(0)

def buscar[A](v:Array[A], cond:A=>Boolean):Int =
  def bucle(i: Int): Int =
    if (i == v.length) -1
    else if (cond(v(i))) i
    else bucle(i + 1)
  bucle(0)

object Principal extends App {

}
