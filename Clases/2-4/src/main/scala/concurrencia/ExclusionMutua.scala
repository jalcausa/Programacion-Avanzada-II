package concurrencia


object ExclusionMutua {
  def main(args: Array[String]): Unit = {
    var c = 0
    /* Esta segunda solución usa una única variable para ver quien puede pasar
       a su sección crítica, como se inicializa a uno de los dos se evita el
       problema de que ambos se queden bloqueados esperando.
       El problema que tiene ahora es que se obliga a que el orden de ejecución
       sea ese, ya que si por ejemplo la hebra 0 no quiere entrar a la sc pero
       la hebra 1 sí quiere entrar no puede acceder hasta que lo haga la 0 y le
       ceda el testigo.
    */
    @volatile var turno = 0
    val h0 = thread{
      for (i<-0 until 100)
        while(turno == 1){log("en el bucle")}
        c += 1 // SC0, aquí turno = 0
        turno = 1
    }
    val h1 = thread{
      for (i <- 0 until 100)
        while(turno == 0){log("en el bucle")}
        c += 1 // SC1, aquí turno = 1
        turno = 0
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