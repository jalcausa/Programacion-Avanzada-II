import scala.collection.mutable

object Contador {
  private val mapa = mutable.Map.empty[String, Int]

  def calcula(s: String): Int = {
    if (mapa.contains(s)) mapa(s)
    else {
      val cont = new Contador
      for (c <- s) {
        cont.inc(c.toInt)
      }
      mapa += s -> cont.valor
      cont.valor
    }
  }

  def log() = println(mapa.mkString("\n"))
}

class Contador {
  private var suma = 0
  def inc(v:Int) =
    suma += v
  def valor = suma
}

// Si ponemos extends App nos evitamos tener que crear el main y que empiece aquí
object Main extends App {
  Contador.calcula("En scala cada valor es un objeto")
  Contador.calcula("Hola Mundo")
  Contador.calcula("función estática")
  Contador.log()
}
