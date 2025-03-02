/*
Implementa una función que compruebe si una cadena dada es un palíndromo
(ignorando mayúsculas y minúsculas y espacios).
Nota: utilizar las funciones de la librería para la clase String.
*/
def palindromo(palabra: String): Boolean =
  val palabra_sin_sp: String = palabra.filter(c => c != ' ')
  val palabra_rev_sin_sp: String = palabra.reverse.filter(c => c != ' ')
  palabra_sin_sp == palabra_rev_sin_sp

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