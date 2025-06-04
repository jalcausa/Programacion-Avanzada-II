package Laboratorio7

import scala.util.Random

// CS-And: espero si está lleno o si al subirme hay una conf. insegura
// CS-iPhone: espero si está lleno o si al subirme hay una conf. insegura
// CS-Todos: espero hasta el final del paseo para bajarme

object Barca{
  private var nIPhone = 0
  private var nAndroid = 0
  private var pEntradaAndroid = true // true abierto, CS-Android
  private var pEntradaIPhone = true // CS-iPhone
  private var pSalida = false // CS-todos

  def paseoIphone(id:Int) = synchronized {

    while (!pEntradaIPhone) wait()
    nIPhone += 1
    log(s"Estudiante IPhone $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    if (nIPhone + nAndroid == 3) {
      if (nIPhone == 3 || nIPhone == 1) pEntradaAndroid = false
      else pEntradaIPhone = false
    }
    if (nIPhone + nAndroid < 4) {
      while (!pSalida) wait()
    } 
    else {
      pEntradaIPhone = false
      pEntradaAndroid = false
      log(s"Empieza el viaje....")
      Thread.sleep(Random.nextInt(200))
      log(s"fin del viaje....")
      pSalida = true
      notifyAll()
    }
    log(s"Estudiante IPhone $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    nIPhone -= 1
    if (nIPhone + nAndroid == 0) {
      pSalida = false
      pEntradaIPhone = true
      pEntradaAndroid = true
      notifyAll()
    }
  }

  def paseoAndroid(id:Int) = synchronized {
    while (!pEntradaAndroid) wait()
    nAndroid += 1
    log(s"Estudiante Android $id se sube a la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    if (nIPhone + nAndroid == 3) {
      if (nAndroid == 3 || nAndroid == 1) pEntradaIPhone = false
      else pEntradaAndroid = false
    }
    if (nIPhone + nAndroid < 4) {
      while (!pSalida) wait()
    } else {
      pEntradaIPhone = false
      pEntradaAndroid = false
      log(s"Empieza el viaje....")
      Thread.sleep(Random.nextInt(200))
      log(s"fin del viaje....")
      pSalida = true
      notifyAll()
    }
    log(s"Estudiante Android $id se baja de la barca. Hay: iphone=$nIPhone,android=$nAndroid ")
    nAndroid -= 1
    if (nIPhone + nAndroid == 0) {
      pSalida = false
      pEntradaIPhone = true
      pEntradaAndroid = true
      notifyAll()
    }
  }
}
object Ejercicio5 {

  def main(args:Array[String]) = {
    val NPhones = 10
    val NAndroid = 10
    val iphone = new Array[Thread](NPhones)
    val android = new Array[Thread](NAndroid)
    for (i<-iphone.indices)
      iphone(i) = thread{
     //   while (true){
          Thread.sleep(Random.nextInt(400))
          Barca.paseoIphone(i)
        //    }
      }
    for (i <- android.indices)
      android(i) = thread {
     //   while (true) {
          Thread.sleep(Random.nextInt(400))
          Barca.paseoAndroid(i)
     //   }
      }
  }
}
