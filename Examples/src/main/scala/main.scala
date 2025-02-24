import scala.collection.mutable
@main
def main(): Unit =
  /*
  El código de abajo es incorrecto, ya que por defecto un Map es inmutable.
  Si queremos poder modificarlo hay que declararlo como mutable
  val capital = Map("US" -> "Washington", "France" -> "Paris")
  capital += ("Japan" -> "Tokyo")
  println(capital("France"))
   */
  // Versión correcta: referencia inmutable (val), estructura mutable
  val capital = mutable.Map("US" -> "Washington", "France" -> "Paris")
  capital += ("Japan" -> "Tokyo")
  println(capital("France"))
  /*
  A continuación se muestra la diferencia entre una clase en Java y una en Scala:
  1) En Java:
  class MyClass { // this is Java
    private int index;
    private String name;
    public MyClass(int index, String name) {
    this.index = index;
    this.name = name;
    }
  }
  2) En Scala:
  class MyClass(index: Int, name: String)

  Por defecto, el compilador de Scala produce una clase con dos variables de instancia
  privadas y un constructor por defecto que toma como argumento ambas.
  La única diferencia es que las variables de instancia generadas en Scala serán final

  En Java cuando creamos un nuevo objeto hay que escribir su tipo dos veces:
  val x: HashMap[Int, String] = new HashMap[Int, String]()
  En Scala eso no es necesario. Cualquiera de las dos siguientes alternativas es válida:
  val x = new HashMap[Int, String]()
  val x: Map[Int, String] = new HashMap()
  */
