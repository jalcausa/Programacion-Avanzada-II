package GUISimple

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, FlowLayout, GridLayout}
import java.util
import javax.swing.{JButton, JFrame, JLabel, JPanel, JScrollPane, JTextArea, JTextField, SwingUtilities, SwingWorker, WindowConstants}

/*
Podemos hacer que todos lo relacionado con calcular los primos lo haga otra hebra a parte de tipo worker
para no tener que hacer eso desde la hebra de controlador
class Worker(n:Int, panel: Panel) extends SwingWorker[java.util.List[Int], Unit] = {

}
*/

class Controlador (panel: Panel) extends ActionListener {

  def esPrimo(n: Int): Boolean = {
    def loop(div: Int): Boolean = {
      if (div*div > n) true
      else if (n % div == 0) false
      else loop(div + 1)
    }
    if (n == 1 || n == 2) true
    else loop(2)
  }

  def listaPrimos(n: Int): java.util.List[Int] = {
    val lista = new util.ArrayList[Int]()

    def loop(i: Int, primo: Int): java.util.List[Int] = {
      if (i == n) lista
      else {
        if (esPrimo(primo)) {
          lista.add(primo)
          loop(i + 1, primo + 1)
        } else
          loop(i, primo + 1)
      }
    }

    loop(0, 1)
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    if (e.getActionCommand.equals("SI")) {
      panel.nuevoMensaje("Sí pulsado")
    } else if (e.getActionCommand.equals("NO")) {
      panel.nuevoMensaje("No pulsado")
    } else { // e.getActionCommand.equals("PRIMOS")
      try {
        val n = panel.numero()
        val lista = listaPrimos(n)
        panel.listaPrimos(lista)
      } catch {
        case e:NumberFormatException => panel.nuevoMensaje("Número incorrecto")
      }
    }
  }
}

class Panel extends JPanel {
  this.setLayout(new BorderLayout())
  private val panelNorte = new JPanel()
  private val etiq = new JLabel("es verdad?")
  private val bsi = new JButton("Sí")
  private val bno = new JButton("No")
  private val etiqPrimos = new JLabel("Primos?:")
  private val nprimos = new JTextField(5)
  private val area = new JTextArea(30, 30)
  // private val scrol = new JScrollPane(area) // Arreglar esto

  private val mensaje = new JLabel("No se ha pulsado ningún botón")

  panelNorte.add(etiq)
  panelNorte.add(bsi)
  panelNorte.add(bno)
  panelNorte.add(etiqPrimos)
  panelNorte.add(nprimos)


  this.add(panelNorte, BorderLayout.NORTH)
  this.add(area, BorderLayout.CENTER)
  this.add(mensaje, BorderLayout.SOUTH)

  // Hay que vincular el controlador con el elemento visual para que
  // cuando ocurra el evento el controlador ejecute la función que lo maneja
  def controlador(ctr: ActionListener) = {
    bsi.addActionListener(ctr);
    bsi.setActionCommand("SI")
    bno.addActionListener(ctr);
    bno.setActionCommand("NO")

    nprimos.addActionListener(ctr);

  }

  def nuevoMensaje(str: String) = {
    mensaje.setText(str)
  }

  def numero(): Int = {
    Integer.parseInt(nprimos.getText)
  }

  def listaPrimos(lista: java.util.List[Int]) = {
    for (i<- 0 until lista.size()) {
      area.append(s"${lista.get((i))} ")
      if ((i+1)%10 == 0) area.append("\n")
    }
  }

  def limpiarArea() = {
    area.setText("")
  }
}

object EjemploGUI {
  def crearGUI(ventana: JFrame) = {
    val panel = new Panel
    val ctr = new Controlador(panel)
    panel.controlador(ctr)

    ventana.setContentPane(panel)
    // Las tres funciones de abajo las ejecutaremos siempre
    ventana.pack()
    ventana.setVisible(true)
    ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  def main(args: Array[String]): Unit = {
    SwingUtilities.invokeLater(
      new Runnable {
        override def run(): Unit =
          val ventana = JFrame(("Un ejemplo"))
          crearGUI(ventana)
      }
    )
  }
}