object Main{
  def max(x: Int, y: Int):Int=
    if (x > y) x else y
  // El tipo Unit es equivalente al tipo void pero es opcional ponerlo
  def main(args:Array[String]):Unit=
    val vector = new Array[String](3)
    vector(0) = "0"
    vector(1) = "1"
    vector (2) = "2"
    //println(array(0))
    for (i<-0 until vector.length) {
      print(s"$i ")
    }
}