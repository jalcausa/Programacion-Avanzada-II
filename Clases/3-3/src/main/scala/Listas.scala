/*
Si tengo una Lista[Animal] y Lista[Gato] por defecto Lista[Gato] no sería
una sublista de Lista[Animal], la herencia solo se da en el nivel más externo.
Poniendo + lo que hacemos es permitir ese tipo de herencia.
*/

enum Lista[+A]:
  case Nula
  case Cons(a: A, r: Lista[A])

object Lista:
  def apply[A](args: A*): Lista[A] =
    if (args.isEmpty) Nula
    else Cons(args(0), apply(args.tail*))

  def longitud[A](l:Lista[A]): Int =
    l match
      case Nula => 0
      case Cons(a, r) => 1 + longitud(r)

// val l = Lista(1, 2, 3)

class Listas extends App{

}
