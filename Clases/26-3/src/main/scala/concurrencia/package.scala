package object concurrencia {
  
  def thread(body: =>Unit):Thread = {
    val t = new Thread{
      override def run = body
    }
    t.start()
    t
  }
  
  def log(msg: String) =
    println(s"${Thread.currentThread().getName}: $msg")
}
