package concurrencia

object Peterson {
  def main(args: Array[String]): Unit = {
    var c = 0
    // f0 indica que la hebra 0 quiere entrar o está en su sección crítica
    // f1 indica que la hebra 0 quiere entrar o está en su sección crítica
    // turno indica quien tiene permiso para entrar
    @volatile var f0 = false
    @volatile var f1 = false
    @volatile var turno = 0
    val h0 = thread{
      for (i<-0 until 100)
        // Si se intercambian estas dos instrucciones ya no se satisface la exclusion mutua
        f0 = true
        turno = 1
        while(f1 && turno == 1){log("en el bucle")}
        c += 1 // SC0
        f0 = false
        // SNC0
    }
    val h1 = thread{
      for (i <- 0 until 100)
        f1 = true
        turno = 0
        while(f0 &&  turno == 0){log("en el bucle")}
        c += 1 // SC1
        f1 = false
        // SNC1
    }
    h0.join()
    h1.join()
    log(s"c = $c")
    /*
      Peterson
      R1: Exclusión mutua
        * Cuando P0 entra en SC0
          (f1 and turno == 1) = false
            Si f1 = false => P1 está en su SCN
            Si turno != 1 => turno = 0 => p1 está ejecutando su preprotocolo
        * Mientras P0 está en SC0 (f0 == true) P1 no puede entrar:    
      R2: Ausencia de livelock: turno no puede ser 0 y 1 a la vez
      R3: Si sólo un proceso quiere entrar, debe poder hacerlo
        * Si P0 quiere entrar y P1 no, puede hacerlo porque f1 = falso,
        el proceso P1 ha puesto f1 como falso antes de entrar en su sección no crítica
      Justicia: si un protocolo acaba de entrar y el otro estaba esperando va a entrar el otro 
      la próxima vez, no se va a colar dos veces el mismo ya que antes de volver a entrar le da
      el turno al otro y como el otro estaba en su while esperando va a entrar seguro 
      
      Supongamos ahora que intercambiamos el orden de las dos primeras instrucciones del
      preprotocolo, es decir, que hago:
        turno = 1
        f0 = true
      en el P0 y en el P1 hago:  
        turno = 0
        f1 = true
      PROBLEMA:
      P0: turno = 1
      P1: turno = 0
      P1: f1 = true
      P1: f0 and turno = 0 == false => entra en su SC0
      P0: f0 = true
      P0: f1 and turno = 1 == false 0 => entra en su SC1
      Violaría el principio de exclusión mutua.
    */
  }