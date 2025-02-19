class Ejemplos {
  /* Listas
  LA diferencia con los arrays es que las listas son inmutables
  */
  val res1 = List(1,2,3)
  val res2 = List(4,5,6)
  // Para concatenar listas:
  val res3 = res1:::res2
  /*
  -Añadir un elemento a la cabeza:
  0::res1
  -Crear una lista concatenando elementos:
  1::4::5::Nil
  -Acceder a la posición 1 de la lista:
  lista.apply(1)
  -Drop crea una nueva lista eliminando
   el número de elementos que le indiques en la cabeza
  -Funcion exists: devuelve un Boolean si algún elemento de la lista verifica la condición
  l.exists((x:Int) => x%2 == 0)
  l.exists((x:Int) => x>3) // Se puede escribir así también:
  l.exists(_>3)
  -forall: comprueba si todos los elementos de la lista verifican la condición
  l.forall((x:Int) => x%2 == 0)
  l.forall(_%2 == 0)
  -filter: genera una nueva lista con los elementos que satisfacen la condición indicada
  l.filter(_%2==0)
  También se puede hacer en negativo:
  l.filterNot(_%2==0)
  -foreach:
  Las dos expresiones siguientes son equivalentes:
  l.foreach((n:Int) => println(s"$n"))
  l.foreach(println)
  El print hace lo mismo que println pero sin el salto de línea
  l.foreach((n:Int) => print(s"$n  "))
  -mkString
  crea un string a partir de una lista con el separador que le indicamos como parámetro
  l.mkString(" ")
  -sortWith
  Le tenemos que decir el criterio que queremos que siga para ordenar la lista
  lista.sortWith((x:Int, y:Int) => x < y)
  -Supongamos que queremos que a partir de una lista nos devuelva los múltiplos de 3 en orden invertido
  Dada l = List(1, 3, 4, 6, 8, 9)
  l.filter(_%3 == 0).reverse

  ------------------------------ MAPAS y CONJUNTOS --------------------------------------------------
  Tanto los mapas como los conjuntos pertenecen a scala.collection
  Dentro de scala.collection tenemos:
  -scala.collection.inmutable
  -scala.collection.mutable
  Tenemos tanto mapas como conjuntos de los dos tipos
  En Scala las intefaces se llaman trails, luego tenemos implementaciones como en Java:
  -HashSet, TreeSet, ...
  Al usar un set por defecto nos da el inmutable, podemos hacer:
  val c = Set(1, 2, 3)
  Si declaro algo como val significa que la REFERENCIA es inmutable, pero no el contenido.
  Es decir, si hago
  val cmutable = scala.collection.mutable.Set(2, 3, 1)
  Puedo cambiar el contenido del Set pero no la referencia
  Por defecto si no hago lo anterior se me crea una colección INMUTABLE
  -La operación ++ sería la unión y & sería la intersección
  Para colecciones se usa casi siempre val para la referencia
  --------------------- CLASES y OBJECTOS --------------------------------------------------
  class Contador {
    private var suma = 0
    def inc(v:Int) =
      suma += v
    def valor = suma
  }
  La función inc es pública por defecto. Por defecto es público lo que declaremos
  Si queremos que sea privado tenemos que poner private.
  En Scala a diferencia de en Java no hay métodos estáticos, es decir no hay métodos de la clase
  que se puedan usar sin instancia del objeto.
  La manera de hacer esto en Scala es cuando definimos la clase creamos un objecto acompañante
  que incluirá lo que sea estático
  Es decir debería crear:

  object Contador{
    private val mapa = mutable.Map.empty[String,Int]
    def calcula(s:String):Int = {
      if (mapa.contains(s)) mapa(s)
      else {
        val cont = new Contador
        for (c<-s)
          cont.inc(c.toInt)
          mapa += s->cont.valor
      cont.valor
 }
  }
  Junto con la definición de la clase
   */
}
