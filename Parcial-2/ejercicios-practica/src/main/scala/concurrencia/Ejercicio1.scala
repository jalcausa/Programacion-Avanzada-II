package concurrencia

import java.util.concurrent.*
import scala.util.Random

object mediciones {

  var nMediciones = 0
  private val mutex = new Semaphore(1)
  private val esperaTrabajador = new Semaphore(0)
  private val esperaSensor = new Array[Semaphore](3)
  for (i <- esperaSensor.indices)
    esperaSensor(i) = new Semaphore(1)

  def nuevaMedicion(id: Int) = {
    esperaSensor(id).acquire()
    mutex.acquire()
    nMediciones += 1
    log(s"Sensor $id almacena su medición" )
    if (nMediciones == esperaSensor.length)
      esperaTrabajador.release()
    mutex.release()
  }

  def leerMediciones() = {
    esperaTrabajador.acquire()
    log(s"El trabajador recoge las mediciones")
  }

  def finTarea() = {
    mutex.acquire()
    nMediciones = 0
    mutex.release()
    log(s"El trabajador ha terminado sus tareas")
    for (i <- esperaSensor.indices)
      esperaSensor(i).release()
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
