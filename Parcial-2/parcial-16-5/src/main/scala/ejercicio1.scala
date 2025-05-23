package PAII_IS_A1

import java.util.concurrent.Semaphore
import scala.util.Random
class Salon(cap:Int){

  /*
   * Condiciones sincronización del ejercicio 1
   * CS-Dec1: El decano no entra en la salon hasta que lo avisan de que se ha excedido el aforo
   * CS-Dec2: El decano espera a que se vacíe el salón para volver a dormir
   * CS-Est1: Un estudiante no puede entrar si el decano está en ella
   */

  private var nEstudiantes = 0
  private var superadoAforo = 0
  private val mutex = new Semaphore(1)

  private val puertaEntrada = new Semaphore(1)

  private val esperaDecano = new Semaphore(0)

  def llegoAFiesta(id:Int)={
    //El estudiante llama a este método cuando quiere entrar en la fiesta
    puertaEntrada.acquire()
    mutex.acquire()
    nEstudiantes += 1
    log(s"Estudiante $id llega a la fiesta. Hay $nEstudiantes")
    //el estudiante que detecta que se ha superado el aforo avisa al decano
    if (nEstudiantes == cap && superadoAforo != 1)
      log(s"            Estudiante $id aviso al Decano")
      esperaDecano.release()
      superadoAforo = 1
    puertaEntrada.release()
    mutex.release()
  }

  def salgoFiesta(id:Int)={
    //estudiante id llama a este método cuando quiere abandonar la fiesta
    mutex.acquire()
    nEstudiantes -= 1
    log(s"Estudiante $id sale de la fiesta. Hay $nEstudiantes")
    if (nEstudiantes == 0 && superadoAforo == 1)
      esperaDecano.release()
    mutex.release()
  }

  def meDuermo()= {
    //El decano llama a este método cuando quiere dormir
    //se despierta cuando le avisan de que se ha superado el aforo
    esperaDecano.acquire()
    log(s"Decano: me despierto")
    log(s"Decano: entro en la sala")
  }
  def esperoTodosFuera()={
    //El decano llama a este método para esperar a que salgan del solón todos los estudiantes
    log(s"Decano: espero que salgan todos")
    puertaEntrada.acquire()
    mutex.acquire()

    if (nEstudiantes > 0)
      mutex.release()
      esperaDecano.acquire()
    else
      mutex.release()
    superadoAforo = 0
    puertaEntrada.release()
    log(s"Decano: me voy otra vez a dormir")
    esperaDecano.acquire()
  }
}
object ejemResidencia {

  def main(args:Array[String]):Unit={
    val R = 20
    val Cap = 5
    val F = 1
    val salon = new Salon(Cap)
    val estudiante = new Array[Thread](R)
    for (i<-estudiante.indices)
      estudiante(i) = thread{
        for (j<-0 until F)
          Thread.sleep(Random.nextInt(700))
          salon.llegoAFiesta(i)
          Thread.sleep(100)
          salon.salgoFiesta(i)
          Thread.sleep(700)
      }
    val decano = thread{
      var fin =false
      while (!fin && !Thread.interrupted()){
        try{
          Thread.sleep(Random.nextInt(200))
          salon.meDuermo()
          salon.esperoTodosFuera()
        } catch {
          case e:InterruptedException => fin = true
        }
      }
    }
    estudiante.foreach(_.join())
    decano.interrupt()
    decano.join()
    log(s"Todos los estudiantes y el decano se han ido a dormir")
  }

}
