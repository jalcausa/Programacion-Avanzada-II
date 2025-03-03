/*
isSorted devuelve si el array dado como parámetro está ordenado
El segundo parámetro que toma es la función que usamos para definir el orden
*/

def isSorted[A](as: Array[A], comp: (A, A) => Boolean): Boolean = {
  def bucle(i: Int):Boolean =
    if (i == as.length - 1) true
    else
      if (!comp(as(i), as(i + 1))) false
      else bucle(i + 1)
  if (as.length == 0 || as.length == 1) true
  bucle(0)
}

// Para probarla: isSorted(Array(1, 2, 3), _<=_)

/* Dada una función que toma dos parámetros fija uno de los parámetros
dejando el segundo libre. Ej: f: RxR -> R, f(x,y) = xy, dada x = a la fija
y devuelve f (y) = ay
*/
def partial[A, B, C](a: A, f: (A, B) => C): B => C =
  (y: B) => f(a, y)

/*
Función curry, dada una función f: A x B => C, la transforma en otra de la forma
A => (B => C)
*/

def curry[A, B, C](f: (A, B) => C): A => (B => C) =
  (x: A) => ((y:B) => f(x,y))

// val h = curry[Int, Int, Int]((x,y)=>x+y)