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
    //val h1 = new Escribe('a') // Aquí ya empieza a entrelazarse el main con el h1
    //val h2 = new Escribe('b')
    //val h3 = new Escribe('c')

    val h1 = new Thread(new EscribeR('a')) // Runnable es una interfaz. Esto equivale a h1. Se puede extender solo de una clase. Esto es como el implements de Java, pues es una clase
    val h2 = new Thread(new EscribeR('b'))
    val h3 = new Thread(new EscribeR('c'))
    // start permite el entrelazado(interleaving) de la ejecución de hilos
    // start() crea un nuevo hilo de ejecución. Internamente, llama al méto-do run() en este nuevo hilo  y permite la ejecución concurrente / paralela
    h1.start() // Es una forma de llamar al código que hay en el run. Si no hay nada, no se ejecuta nada
    h2.start()
    h3.start()
    // Vemos el resultado del entrelazado. Cada vez sale una cosa

    // Si pusiese run en vez de start en las tres, saldrían primero todas las a, después todas las b, y después todas las c
    // Es el propio main el que ejecuta to-do secuencialmente

    h1.join() // main no avanza hasta que no termine h1
    h2.join()
    h3.join()

    println(s"${Thread.currentThread().getName}: Fin del programa")

    /*

    Lo que ponía antes:

    println(s"${Thread.currentThread().getName}: Principal") // El main es también una hebra
    // Aunque salga al principio, no debería, podría salir incluso al final

     */

  }
}

