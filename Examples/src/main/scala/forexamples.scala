/*
En Scala hay muchas formas de definir un bucle for veremos algunas:
1) for-each:
@main def m(args: String*) =
  args.foreach(arg => println(arg))
2) for-each de manera más concisa:
@main def m(args: String*) =
  args.foreach(println)
3) Otra forma de bucle for:
@main def m(args: String*) =
  for arg <- args do
    println(arg)
 */
@main def m(args: String*) =
  for arg <- args do
    println(arg)