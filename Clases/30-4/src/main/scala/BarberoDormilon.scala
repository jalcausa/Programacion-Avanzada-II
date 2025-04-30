/*
Tenemos una serie de clientes esperando para ser pelados y un barbero
que solo puede afeitar a un cliente a la vez.
Si no hay ningún cliente el barbero duerme un rato.

Cuando un cliente llega entra a la sala de espera (infinita) y espera a ser
atendido.

Necesitamos conocer cuántos clientes hay esperando en la sala de espera,
lo podemos representar con una variable private var n en el object Barberia.

Necesitamos también un semáforo para controlar la espera y otro para garantizar la
exclusión mutua sobre la variable n.

El semáforo espera quiero que se quede siempre a 0 para que cuando yo haga un
acquire me bloquee
 */