package GUISimple

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, FlowLayout, GridLayout}
import javax.swing.{JButton, JFrame, JLabel, JPanel, JScrollPane, JTextArea, JTextField, WindowConstants}

class Controlador extends ActionListener {

  override def actionPerformed(e: ActionEvent): Unit = {
    if (e.getActionCommand.equals("SI")) {
      // Codigo SI
    } else {
      // Codigo NO
    }
  }

}

class Panel extends JPanel {
  private val etiq = new JLabel("es verdad?")
  private val bsi = new JButton("Sí")
  private val bno = new JButton("No")
  this.add(etiq)
  this.add(bsi)
  this.add(bno)

  // Hay que vincular el controlador con el elemento visual para que
  // cuando ocurra el evento el controlador ejecute la función que lo maneja
  def controlador(ctr: ActionListener) = {
    bsi.addActionListener(ctr);
    bsi.setActionCommand("SI")
    bno.addActionListener(ctr);
    bno.setActionCommand("NO")
  }
}

class Panel2 extends JPanel {
  this.setLayout(new BorderLayout())

  private val etiqTam = new JLabel("tamaño?:")
  private val tam = new JTextField(4)
  private val botonNorte = new JButton("Norte")
  private val panelNorte = new JPanel()
  panelNorte.add(botonNorte); panelNorte.add(etiqTam); panelNorte.add(tam)

  private val botonSur = new JButton("Sur")

  private val areaCentro = new JTextArea(30, 20)
  private val scrollArea = new JScrollPane(areaCentro)

  private val botonEste = new JButton("Este")
  private val botonOeste1 = new JButton("Oeste 1")
  private val botonOeste2 = new JButton("Oeste 2")

  this.add(panelNorte, BorderLayout.NORTH)
  this.add(botonSur, BorderLayout.SOUTH)
  this.add(botonEste, BorderLayout.EAST)
  this.add(scrollArea, BorderLayout.CENTER)

  private val panelOeste = new JPanel()
  panelOeste.setLayout(new GridLayout(2, 1))
  panelOeste.add(botonOeste1)
  panelOeste.add(botonOeste2)
  this.add(panelOeste, BorderLayout.WEST)
}

object EjemploGUI {
  def main(args: Array[String]): Unit = {
    val ventana = JFrame(("Un ejemplo"))
    val panel = new Panel
    val ctr = new Controlador
    panel.controlador(ctr)

    ventana.setContentPane(panel)
    // Las tres funciones de abajo las ejecutaremos siempre
    ventana.pack()
    ventana.setVisible(true)
    ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }
}