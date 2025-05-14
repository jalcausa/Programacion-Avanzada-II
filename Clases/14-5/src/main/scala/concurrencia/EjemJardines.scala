package concurrencia

/*
-Cuando ponemos synchronized nos estamos refieriendo al lock del
objeto que queremos usar
-Cada vez que se hace un wait() hay que anidarlo en un while:
while(!condicion)
  wait()
 */

object Jardin {
  private var n = 0
  def inc = synchronized {
    n += 1
  }
}


object EjemJardines {
  def main(args: Array[String]) =
    val p0 = thread{
      for (i <- 0 until 100) {
        Jardin.inc
      }
    }

    val p1 = thread {
      for (i <- 0 until 100)
        Jardin.inc
    }
}