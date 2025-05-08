/*
CS-Escritor: utilizan la BD en exclusión mutua
CS-Lector: Varios lectores pueden usar simultáneamente la BD

ESCRITORES
while (true)
  BD.openE(i)
  // Aquí tenemos acceso a la BD
  BD.closeE(i)

LECTORES
while (true)
  BD.openL(i)
  // Aquí tenemos acceso a la BD
  BD.closeL(i)

Necesito implementar el objeto BD que tenga las funciones:
  -openE y closeE para los escritores
  -openL y closeL para los lectores

La diferencia con ProductorConsumidor es que aquí podemos tener varios lectores
a la vez en la BD y necesitamos separar preprotocolo y postprotocolo

El primer lector que llega se tiene que esperar si hay un escritor

SOLUCION INJUSTA (si hay muchos lectores nunca puede entrar el escritor):

Object BD {
  private var nLectores = 0 // Para saber cuántos lectores hay en la BD
  private var mutex = new Semaphore(1)
  private val escribiendo = new Semaphore(1) // Sirve para sincronizar ambos

  def openE
    escribiendo.acquire()

  def closeE
    escribiendo.release()

  def openL
    mutex.acquire()
    nLectores += 1
    if (nLectores == 1) // Si soy el primero tengo que ver si hay algún escritor
      escribiendo.acquire()
    mutex.release()

  def closeL
    mutex.acquire()
    nLectores -= 1
    if (nLectores == 0)
      escribiendo.release()
    mutex.release()
}

SOLUCION JUSTA (para permitir entrar escritores):
Mientras no haya escritores dejo entrar a lectores pero en cuanto llega
un escritor no dejo entrar a ningún lector más y cuando salgan todos los
lectores que había entra el escritor

object BD {

  private var nLectores = 0 // Para saber cuántos lectores hay en la BD
  private var mutex1 = new Semaphore(1)
  private val escribiendo = new Semaphore(1) // Sirve para sincronizar ambos
  private var nEsc = 0 // Número de escritores que hay esperando
  private var mutex2 = new Semaphore(1)
  private val hayEscritoresEsperando = new Semaphore(1)
  private val mutex3 = new Semaphore(1)

  def openE
    mutex2.acquire()
    nEsc += 1
    if (nEsc == 1)
      hayEscritoresEsperando.acquire()
    mutex2.release()
    escribiendo.acquire()

  def closeE
    escribiendo.release()
    mutex2.release()
    nEsc -= 1
    // Si no hay más escritores esperando dejo entrar a los lectores
    // mientras haya escritores los dejo entrar
    if (nEsc == 0)
      hayEscritoresEsperando.release()
    mutex2.release()

  def openL
    // Necesito este mutex para que cuando un escritor termine
    // no despierte a un lector si hay escritores esperando, si no que
    // despierte a un escritor. Es decir, que los lectores no se queden
    // bloqueados en hayEscritoresEsperando sino arriba
    mutex3.acquire()
    hayEscritoresEsperando.acquire()
    mutex1.acquire()
    nLectores += 1
    if (nLectores == 1) // Si soy el primero tengo que ver si hay algún escritor
      escribiendo.acquire()
    hayEscritoresEsperando.release()
    mutex3.release()

  def closeL
    mutex1.acquire()
    nLectores -= 1
    if (nLectores == 0)
      escribiendo.release()
    mutex1.release()
}
 */