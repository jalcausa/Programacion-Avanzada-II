package Relacion4
import scala.annotation.tailrec
import scala.util.Random

@tailrec
def tTrue(l: List[Boolean]): Boolean = l match
  case Nil => true
  case true::r => tTrue(r)
  case _ => false

object todosTrue {

  def main(str:Array[String]): Unit =
    val lista = List.fill(5)(Random.nextBoolean())
    val (l1, l2) = lista.splitAt(lista.size/2)
    val (b1, b2) = parallel(
      tTrue(l1), tTrue(l2)
    )
    println(lista)
    println(s"${b1 && b2}")
}