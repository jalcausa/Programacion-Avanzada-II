// Si no ponemos nada antes de num y den son privados e inmutables los atributos
// Si queremos que sean públicos hay que poner val antes
object Racional{
  // Los apply son como constructores
  def apply(num:Int,den:Int):Racional = {
    new Racional(num,den)
  }
  def apply(num:Int):Racional = {
    new Racional(num)
  }
}

class Racional(num:Int, den:Int) {
  // Si tenemos código ejecutable dentro de una clase este forma parte del constructor
  // por defecto, es decir se ejecuta al crear cualquier nuevo objeto
  //println(s"$num/$den")
  require(den != 0)
  private val div = gcd(num, den)
  val n = num / div
  val d = den / div

  def this(n: Int) = this(n, 1)

  //println(s"racional $num/$den creado")
  override def toString: String = s"$n/$d"

  def +(rac: Racional): Racional =
    new Racional(rac.n * den + num * rac.d, d * rac.d)

  def *(rac: Racional): Racional =
    new Racional(rac.n * n, d * rac.d)

  private def gcd(n: Int, m: Int): Int =
    if (m == 0) n else gcd(m, n % m)
}

object Principal extends App{
  val r = new Racional(1,2)
  println(r)
}