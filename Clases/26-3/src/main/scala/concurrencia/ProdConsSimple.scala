package concurrencia

// Aquí no hay un problema de exclusión mutua como en el de los Jardines
class BufferSimple {
  private var c = 0
  // volatile le dice al programa que nunca almacene la variable en la zona de trabajo
  // le dice que la almacene en la zona compartida de los hilos
  @volatile private var hayDato = false
  def nuevoDato(dato: Int): Unit =
    while(hayDato){} // Condición espera productor
    c = dato
    log(s"Productor $c")
    hayDato = true

  def leerDato(): Int =
    while(!hayDato){}  // Condición de espera consumidor. Espera activa!!
    val aux = c
    log(s"Consumidor:           ${aux}")
    hayDato = false
    aux
}
/*
Tenemos dos problemas, que hay datos producidos que no los consume el consumidor
y que hay datos producidos por el productor que son consumidos varias veces por
el consumidor.
-El consumidor debe esperar hasta que haya un nuevo dato.
-El productor debe esperar hasta que el consumidor haya extraído el dato anterior.
*/
object ProdConsSimple {
  def main(args: Array[String]): Unit = {
    val buffer = new BufferSimple

    val prod = thread{
      for (i<-0 until 50)
        // Los log están desincronizados y pueden intercalarse
        //log(s"Productor $i")
        buffer.nuevoDato(i)
    }

    val cons = thread{
      for (i<-0 until 50)
        /* Los log están desincronizados y pueden intercalarse
          para arreglarlo es mejor meterlo dentro de la parte crítica
         */
        //log(s"Consumidor:           ${buffer.leerDato()}")
        buffer.leerDato()
    }
    prod.join()
    cons.join()
  }
}