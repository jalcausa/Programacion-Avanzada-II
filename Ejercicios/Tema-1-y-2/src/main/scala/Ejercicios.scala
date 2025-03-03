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