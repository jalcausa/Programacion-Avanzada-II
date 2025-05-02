package concurrencia

import java.util.concurrent.*
import scala.util.Random

object mediciones {
  // CS-Sensor-i: sensor i no puede volver a medir hasta que el trabajador no ha
  // terminado de procesar las medidas anteriores
  // CS-Trabajador: no puede realizar su tarea hasta que no están las
  // tres mediciones

  private var numMed = 0
  private val mutex = new Semaphore(1)
  private val esperoMediciones = new Semaphore(0) // CS-Trabajador
  private val esperoSensor = new Array[Semaphore](3) // CS-Sensor i
  for (i <- esperoSensor.indices)
    esperoSensor(i) = new Semaphore(0)
  
  def nuevaMedicion(id: Int) = {
    mutex.acquire()
    numMed += 1
    mutex.release()
    log(s"Sensor $id almacena su medición" )
    if (numMed == esperoSensor.length)
      esperoMediciones.release()
    esperoSensor(id).acquire() // CS-sensor id
  }

  def leerMediciones() = {
    esperoMediciones.acquire()
    log(s"El trabajador recoge las mediciones")
    numMed = 0 // No hace falta mutex porque el resto están bloqueados
  }

  def finTarea() = {
    log(s"El trabajador ha terminado sus tareas")
    for  (i <- esperoSensor.indices)
      esperoSensor(i).release()
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
