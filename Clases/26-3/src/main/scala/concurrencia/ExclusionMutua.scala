package concurrencia
// Este código satisface el requisito de exclusión mutua
// pero tiene el problema de que se pueden quedar bloqueados
// si ambos quieren entrar a la vez
object ExclusionMutua {
  def main(args: Array[String]): Unit = {
    var c = 0
    // f0 indica que la hebra 0 quiere entrar o está en su sección crítica
    // f1 indica que la hebra 0 quiere entrar o está en su sección crítica
    @volatile var f0 = false
    @volatile var f1 = false
    val h0 = thread{
      for (i<-0 until 100)
        f0 = true
        while(f1){log("en el bucle")}
        c += 1 // SC0
        f0 = false
    }
    val h1 = thread{
      for (i <- 0 until 100)
        f1 = true
        while(f0){log("en el bucle")}
        c += 1 // SC1
        f1 = false
    }
    h0.join()
    h1.join()
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