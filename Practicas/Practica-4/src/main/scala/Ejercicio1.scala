/*
Ejercicio 1 a)
class Hebra(t: Int, c: Char) extends Thread{
  override def run(): Unit =
    for(i <- 0 until t)
      print(c)
}

object Ejemplos {
  def main(args: Array[String]) =
    val h1 = new Hebra(10, 'A')
    val h2 = new Hebra(10, 'B')
    val h3 = new Hebra(10, 'C')
    h1.start()
    h2.start()
    h3.start()
}
 */

// Ejercicio 1 b)
var turno = 0
class Hebra(id: Int, t: Int, c: Char) extends Thread{
  override def run(): Unit =
    var iter = 0
    for(i <- 0 until t)
      while (turno != id) Thread.sleep(0) // Se pone sleep para que no esté sin hacer nada e intentar
      // que se provoque un cambio de contexto y pueda entrar
      print(c)
      iter += 1
      if (iter == id + 1)
        turno = (turno + 1) % 3
        iter = 0
}

object Ejemplos {
  def main(args: Array[String]) =
    val h0 = new Hebra(0, 10, 'A')
    val h1 = new Hebra(1, 20, 'B')
    val h2 = new Hebra(2, 30, 'C')
    h0.start()
    h1.start()
    h2.start()
}