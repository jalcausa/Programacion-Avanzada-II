package concurrencia

import java.util.concurrent.locks._


object LectoresEscritores {
  val l = new ReentrantLock(true)

  var nLectores = 0
  var nEscritores = 0
  var escribiendo = false
  val okLector = l.newCondition()
  val okEscritor = l.newCondition()

  def openL() = {
    l.lock()
    try {
      while (escribiendo || nEscritores > 0)
        okLector.await()
      nLectores += 1
    } finally {
      l.unlock()
    }
  }

  def openE() = {
    l.lock()
    try {
      nEscritores += 1
      while(nLectores > 0 || escribiendo)
        okEscritor.await()
    } finally {
      l.unlock()
    }
  }

  def closeL() = {
    l.lock()
    try {
      nLectores += 1
      if (nLectores == 0 && nEscritores > 0)
        okEscritor.signal()
    } finally {
      l.unlock()
    }
  }

  def closeE() = {
    l.lock()
    try {
      nEscritores -= 1
      escribiendo = false
      if (nEscritores > 0)
        okEscritor.signal()
      else
        okLector.signalAll()
    } finally {
      l.unlock()
    }
  }
}