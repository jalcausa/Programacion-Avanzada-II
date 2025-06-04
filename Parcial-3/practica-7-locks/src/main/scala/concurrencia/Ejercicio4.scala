package concurrencia

import java.util.concurrent.locks.ReentrantLock
import scala.util.Random

class Coche(C:Int) extends Thread{
  //CS-pasajero1: si el coche está lleno, un pasajero no puede subir al coche hasta que haya terminado
  //el viaje y se hayan bajado los pasajeros de la vuelta actual
  //CS-pasajero2: un pasajero que está en el coche no se puede bajarse hasta que haya terminado el viaje
  //CS-coche: el coche espera a que se hayan subido C pasajeros para dar una vuelta

  private val l = new ReentrantLock(true)

  // CS-Viajeros
  private var pEntradaAbierta = true
  private val cPuertaEntrada = l.newCondition()

  private var pSalidaAbierta = false
  private val cPuertaSalida = l.newCondition()

  // CS-Coche
  private var lleno = false
  private val cLleno = l.newCondition()

  private var numPas = 0
  


  def nuevoPaseo(id:Int)= {
    //el pasajero id quiere dar un paseo en la montaña rusa
    l.lock()
    try {
      while (!pEntradaAbierta) cPuertaEntrada.await()
      numPas += 1
      if (numPas == C) {
        pEntradaAbierta = false
        lleno = true
        cLleno.signal()
      }
      log(s"El pasajero $id se sube al coche. Hay $numPas pasajeros.")
      while(!pSalidaAbierta) cPuertaSalida.await()
      numPas -= 1
      if (numPas == 0) {
        pSalidaAbierta = false
      }
      log(s"El pasajero $id se baja del coche. Hay $numPas pasajeros.")
    } finally {
      l.unlock()
    }
  }

  def esperaLleno =  {
    //el coche espera a que se llene para dar un paseo
    l.lock()
    try {
      pEntradaAbierta = true
      cPuertaEntrada.signalAll()
      while (!lleno) cLleno.await()
      lleno = false
      log(s"        Coche lleno!!! empieza el viaje....")
    }  finally {
      l.unlock()
    }
  }

  def finViaje =  {
    //el coche indica que se ha terminado el viaje
    l.lock()
    try {
      log(s"        Fin del viaje... :-(")
      pSalidaAbierta = true
      cPuertaSalida.signalAll()
    } finally {
      l.unlock()
    }
    
  }

  override def run = {
    var fin = false
    while (!Thread.interrupted() && !fin){
      try {
        esperaLleno
        Thread.sleep(Random.nextInt(Random.nextInt(500))) //el coche da una vuelta
        finViaje
      } catch {
        case e: InterruptedException => fin = true
      }
    }
  }
}
object Ejercicio4 {
  def main(args:Array[String])=
    val coche = new Coche(5)
    val pasajero = new Array[Thread](20)
    coche.start()
    for (i<-0 until pasajero.length)
      pasajero(i) = thread{
          Thread.sleep(Random.nextInt(500))
          coche.nuevoPaseo(i)
      }
    pasajero.foreach(_.join())
    coche.interrupt()
    coche.join()
    log("Fin del programa")
}
