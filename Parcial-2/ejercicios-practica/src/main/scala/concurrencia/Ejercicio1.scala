package concurrencia

import java.util.concurrent.*
import scala.util.Random

object mediciones {
  // CS-Sensor-i: sensor i no puede volver a medir hasta que el trabajador no ha
  // terminado de procesar las medidas anteriores
  // CS-Trabajador: no puede realizar su tarea hasta que no están las
  // tres mediciones


  def nuevaMedicion(id: Int) = {
    // ...
    log(s"Sensor $id almacena su medición" )
    // ...
  }

  def leerMediciones() = {
    // ...
    log(s"El trabajador recoge las mediciones")
    // ...
  }

  def finTarea() = {
    // ...
    log(s"El trabajador ha terminado sus tareas")
    // ...
  }
}

object Ejercicio1 {
  def main(args: Array[String]) =
    val sensor = new Array[Thread](3)

    for (i <- 0 until sensor.length)
      sensor(i) = thread {
        while (true)
          Thread.sleep(Random.nextInt(100)) // midiendo
          mediciones.nuevaMedicion(i)
      }

    val trabajador = thread {
      while (true)
        mediciones.leerMediciones()
        Thread.sleep(Random.nextInt(100)) // realizando la tarea
        mediciones.finTarea()
    }
}
