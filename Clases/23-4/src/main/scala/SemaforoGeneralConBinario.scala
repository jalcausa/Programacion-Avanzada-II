/*
Es posible representar un semáforo general usando semáforos binarios de la siguiente manera
val espera = New Semaphore () // Binario
val valor = ? // Aquí puedo guardar el número que habría en el semáforo general
Necesito que mi semáforo binario tenga 1 siempre que el semáforo general sea mayor que 0
Necesito "comprimir" todos los números a partir del 1 para que siempre esté el semáforo
cerrado cuando no vale 0 el general

Para implementar un semáforo general con semáforos binario necesito un mutex para garantizar
la exclusión mutua cuando vaya a incrementar o decrementar el valor (con acquire() y release())
ya lo tengo asegurado pero aquí no.

Condición:
espera = 0 si y sólo si valor = 0

Class SemGen(n: Int) {

  private val espera = new Semaphore(0) // Semáforo binario
  private val valor = n // Representa el valor que tendría el semáforo general
  
  if (n != 0)
    espera.release()
  
  def acquireGen(){
    espera.acquire()
    // Si hemos llegado aquí es porque espera = 1 y valor > 0
    mutex.acquire()
    valor -= 1
    if (valor > 0)
      espera.release()
    mutex.release()
  }
  
  def releaseGen(){
    mutex.acquire()
    valor += 1
    if (valor == 1)
      espera.release()
    mutex.release()
  }
}

EJERCICIO IMPRESORAS:
Tenemos dos impresoras y varios usuarios y necesitamos garantizar la exclusión mutua
 */