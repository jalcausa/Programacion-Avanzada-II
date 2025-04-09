package Relacion4

// Apartado a)

def parallel[A, B](a: => A, b: => B): (A, B) =
  var va = null.asInstanceOf[A] // Estoy declarando una variable de tipo A y la pongo a null
  var vb = null.asInstanceOf[B] // Estoy declarando una variable de tipo B y la pongo a null

  val ha = thread{
    va =  a
  }

  val hb = thread{
    vb = b
  }
  (va,vb)

// Apartado b)