// Ejercicio 2

// Utilizando match y funciones de orden superior, define una función combinations que reciba un
// número k y una lista, y devuelva todas las combinaciones de tamaño k
// de los elementos de la lista. Por ejemplo, si k=2 y la lista es List(1, 2, 3, 4), debería devolver
// List(List(1, 2), List(1, 3), List(1, 4), List(2, 3), List(2, 4), List(3, 4)).

def combinations[A](k: Int, list: List[A]): List[List[A]] = {
  list.foldRight(List(List[A]()))((elem, acc) => acc ++ acc.map(l => elem::l)).filter(l => l.length == k)
}

// Examples
combinations(2, List(1, 2, 3, 4)) == List(List(1, 2), List(1, 3), List(1, 4), List(2, 3), List(2, 4), List(3, 4))
combinations(3, List(1, 2, 3, 4)) == List(List(1, 2, 3), List(1, 2, 4), List(1, 3, 4), List(2, 3, 4))

// Ejercicio 3

// Dada la clase

class Person(name: String, age: Int) {
  def getAge = age
  def getName = name
  override def toString: String = s"Person($name, $age)"
}

// y un valor

val people = List(
    Person("Alice", 30), Person("Andrew", 25), Person("Charlie", 30),
    Person("Catherine", 40), Person("Eve", 35), Person("Edward", 28),
    Person("Grace", 32), Person("George", 45), Person("Ivy", 29),
    Person("Isaac", 33), Person("Karen", 27), Person("Kyle", 30),
    Person("Mona", 25), Person("Michael", 30), Person("Oscar", 36)
  )

// crea un map en el que las claves son las iniciales de los nombres y los valores son conjuntos
// de los nombres de las personas con esa inicial

val byInitial: Map[Char, Set[String]] = people.map(p => p.getName).groupBy(p => p.charAt(0)).map((l, s) => (l, s.toSet))

// Output: HashMap(E -> Set(Eve, Edward), A -> Set(Alice, Andrew), M -> Set(Mona, Michael), I -> Set(Ivy, Isaac), G -> Set(Grace, George), C -> Set(Charlie, Catherine), K -> Set(Karen, Kyle), O -> Set(Oscar))

// crea un map en el que a cada persona se le asocia su edad en meses
val agesInMonthsMap: Map[String, Int] = {
  people.groupBy(p => p.getAge).map((edad, p) => (p.map(_.getName).head, edad*12))
}

// val agesInMonthsMap: Map[String, Int] = HashMap(Karen -> 324, Edward -> 336, George -> 540, Grace -> 384, Alice -> 360, Mona -> 300, Oscar -> 432, Michael -> 360, Ivy -> 348, Charlie -> 360, Kyle -> 360, Eve -> 420, Andrew -> 300, Catherine -> 480, Isaac -> 396)

// calcula la edad media de las personas
val averageAge: Double = people.map(p => p.getAge).sum / people.length

// Output: val averageAge: Double = 31.666666666666668

// Ejercicio 4

// Sin utilizar la función partition, implementa una función partitionBy que particione una lista
// en dos listas según un predicado, es decir, coloque los elementos que cumplen el predicado en
// una lista y los que no lo cumplen en otra.

def partitionBy[A](xs: List[A])(pred: A => Boolean): (List[A], List[A]) = {
  xs.foldRight((List[A](), List[A]())){(elem, acc) =>
    if pred(elem) then (elem::acc._1, acc._2)
    else (acc._1, elem::acc._2)
  }
}

//partitionBy(List(1, 2, 3, 4, 5, 6))(_ % 2 == 0) == (List(2, 4, 6), List(1, 3, 5))
//partitionBy(List("apple", "banana", "cherry", "date"))(_.length > 5) == (List("banana", "cherry"), List("apple", "date"))


