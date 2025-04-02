package concurrencia

object Jardines {
  def main(args: Array[String]) = {
    var c = 0
    val p1 = thread{
      for (i<-0 until 100)
        c += 1
    }
    val p2 = thread{
      for (i <- 0 until 100)
        c += 1
    }
    p1.join()
    p2.join()
    log(s"c = $c")
    /*
    El problema aquí es que el c += 1 se divide en tres instrucciones máquina
    load c
    inc c
    store c
    y estas tres instrucciones se van entrelazando en las dos hebras lo que hace
    que el resultado no sea el esperado
     */
  }
}