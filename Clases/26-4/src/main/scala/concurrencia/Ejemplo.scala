package concurrencia
import scala.util.Random

// Thread: clase que representa un hilo de ejecución
class Escribe(c:Char) extends Thread{

  override def run =
    for (i<-0 until 10)
      println(c)

}
/* Un runnable es un objeto que se puede convertir en una hebra
  Permite separar la lógica de ejecución de la gestión del hilo
*/
// Runnable: Una interfaz que define la tarea a ejecutar en un hilo
// Runnable es más flexible para herencia múltiple en Scala
class EscribeR(c:Char) extends Runnable{
  override def run =
    for (i <- 0 until 10)
      println(s"${Thread.currentThread().getName}: $c") // Poner el nombre del hilo es interesante para la depuración.
  // Enumera las hebras de cero en adelante según el orden de creación
  Thread.sleep(Random.nextInt(1000))
  // Si pongo Thread.sleep(0), me voy del procesador. Quiero que entre otro, vaya. Pero podría volver a entrar el mismo
  // Si lo pongo, hay más permutaciones, no salen tantos chorros de una misma letra
}
/*
Thread vs Runnable
Thread

Es una clase concreta que representa un hilo de ejecución
  Tiene métodos propios como start(), run(), join(), etc.
  Cuando heredas de Thread, estás creando una subclase de hilo

Runnable

Es una interfaz que define un contrato para la ejecución de una tarea
Solo tiene un méto-do run()
No tiene métodos de gestión de hilos como start() o join()

Métodos
Thread

start(): Inicia la ejecución del hilo, llamando internamente a run()
run(): Méto-do que contiene el código a ejecutar en el hilo
  Tiene métodos adicionales como join(), sleep(), interrupt(), etc.

  Runnable

Solo tiene run(): Define la tarea a ejecutar
No tiene métodos de control de hilo propios
*/

object Principal{
  def main(args:Array[String]): Unit = { // La hebra main es una adicional a las que he creado
    def escribir(str: String, n:Int) =
      for (i <- 0 until n)
        log(str)
    val h1 = thread(
      escribir("a", 10)
    )  
    val h2 = thread(
      escribir("b", 10)
    )
  }
}

