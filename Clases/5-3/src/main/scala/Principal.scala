enum Lista[+A]:
  case Nula
  case Cons(a: A, r: Lista[A])

// Para usar las funciones que aparecen abajo tenemos que hacer antes
// import Lista._ desde la REPL
object Lista:
  def apply[A](args: A*): Lista[A] =
    if (args.isEmpty) Nula
    else Cons(args(0), apply(args.tail*))

  def longitud[A](l:Lista[A]): Int =
    l match
      case Nula => 0
      case Cons(a, r) => 1 + longitud(r)

  def sumaR(l:Lista[Int]): Int =
    l match
      case Nula => 0
      case Cons(head, tail) =>  head + sumaR(tail)

  def sumaL(l:Lista[Int]): Int =
    def bucle(l: Lista[Int], acc: Int): Int = l match
      case Nula => acc
      case Cons(a, r) => bucle (r, a + acc)
    bucle(l, 0)

  def productoL(l: Lista[Int]): Int =
    def bucle(l: Lista[Int], acc: Int): Int = l match
      case Nula => acc
      case Cons(a, r) => bucle(r, a * acc)
    bucle(l, 1)

  def operarR[A](l: Lista[A], inic: A, f:(A, A) => A): A =
    l match
      case Nula => inic
      case Cons(head, tail) => f(head, operarR(tail, inic, f))

  def operarL[A](l: Lista[A], inic: A, f:(A, A) => A) =
    def bucle(l: Lista[A], acc: A): A = l match
      case Nula => acc
      case Cons(a, r) => bucle(r, f(a, acc))
    bucle(l, inic)

  def foldRight[A, B](l: Lista[A], inic: B, f: (A,B) => B): B = l match
    case Nula => inic
    case Cons(a, r) => f(a, foldRight(r, inic, f))

  def foldLeft[A, B](l: Lista[A], inic: B, f: (B, A) => B): B =
    def bucle(l: Lista[A], acc: B): B = l match
      case Nula => acc
      case Cons(a, r) => bucle(r, f(acc, a))
    bucle(l, inic)

// Definir la función sumR usando foldRight
// foldRight(Lista('1', '2', '3'), 0 , (x,y) => y+1)

// En List tenemos predefinida la función foldRight y foldLeft
// List(1,2,3).foldRight(0)((x, y) => y + 1)

// Para invertir una lista:
// foldLeft[Int, Lista[Int]](Lista(1,2,3), Nula, Cons(_,_))

// Solución no recursiva de cola:
def cadenasNRC(n: Int): List[String] =
  def bucle(n: Int, acc: String, listaCadena: List[String]): List[String] =
    if (n == 0) acc::listaCadena
    else bucle(n - 1, acc + "1", listaCadena) ::: bucle(n-1, acc + "0", listaCadena)
  bucle(n, "", Nil)

// Solución recursiva de cola:
def cadenasBinaria(n: Int): List[String] =
  def incrementa(l: List[String]): List[String] =
    l.map[String](_ + "0") ++ l.map[String](_ + "1")
  @annotation.tailrec
  def bucle(m: Int, acc: List[String]): List[String] =
    if (m == n) acc
    else bucle(m + 1, incrementa(acc))
  bucle(0, List(""))