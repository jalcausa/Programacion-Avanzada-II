package Relacion4

import scala.util.Random

def mezcla(l1: List[Int], l2: List[Int]): List[Int] = (l1, l2) match {
  case (_, Nil) => l1
  case (Nil, _) => l2
  case(a::rl1, b::rl2) =>
    if (a <= b) a::mezcla(rl1, l2)
    else b::mezcla(l1, rl2)
}

def ordenar(l: List[Int]): List[Int] = {
  if (l.size <= 1) l
  else {
    val (l1, l2) = l.splitAt(l.size/2)
    val (l1o, l2o) = parallel(ordenar(l1), ordenar(l2))
    mezcla(l1o, l2o)
  }
}
// Falla algo en 
object Ordenacion {
  def main(args: Array[String]) = {
    val lista = List.fill(Random.nextInt(50))(Random.nextInt(100))
    log(s"$lista")
    log(s"${ordenar(lista)}")
  }
}