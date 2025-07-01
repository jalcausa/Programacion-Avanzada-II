import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, GridLayout}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.util
import javax.swing.{JButton, JFrame, JLabel, JPanel, JProgressBar, JScrollPane, JTextArea, JTextField, SwingUtilities, SwingWorker, WindowConstants}
import scala.collection.mutable.ListBuffer
import scala.concurrent.CancellationException

// Clase para contener pares de números primos con su posición
case class Primos(a: Int, b: Int, posicion: Int)

// Worker para números primos twin (distancia 2)
class TwinWorker(n: Int, panel: Panel) extends SwingWorker[Unit, Primos] {

  def esPrimo(n: Int): Boolean = {
    def loop(div: Int): Boolean = {
      if (div * div > n) true
      else if (n % div == 0) false
      else loop(div + 1)
    }
    if (n <= 1) false
    else if (n == 2) true
    else loop(2)
  }

  def calcularTwinPrimos(n: Int): Unit = {
    var count = 0
    var candidate = 3

    while (count < n && !this.isCancelled) {
      if (esPrimo(candidate) && esPrimo(candidate + 2)) {
        publish(Primos(candidate, candidate + 2, count))
        count += 1
        this.setProgress(count * 100 / n)
      }
      candidate += 2 // Solo números impares después del 2
    }
  }

  override def doInBackground(): Unit = {
    this.setProgress(0)
    calcularTwinPrimos(n)
  }

  override def done(): Unit = {
    try {
      if (!this.isCancelled) {
        panel.updateTwinMessage("calculando primos twin.. terminado")
      }
    } catch {
      case _: CancellationException => // No cambiar el mensaje si fue cancelado
    }
  }

  override def process(chunks: util.List[Primos]): Unit = {
    panel.addTwinPrimos(chunks)
  }
}

// Worker para números primos cousin (distancia 4)
class CousinWorker(n: Int, panel: Panel) extends SwingWorker[Unit, Primos] {

  def esPrimo(n: Int): Boolean = {
    def loop(div: Int): Boolean = {
      if (div * div > n) true
      else if (n % div == 0) false
      else loop(div + 1)
    }
    if (n <= 1) false
    else if (n == 2) true
    else loop(2)
  }

  def calcularCousinPrimos(n: Int): Unit = {
    var count = 0
    var candidate = 3

    while (count < n && !this.isCancelled) {
      if (esPrimo(candidate) && esPrimo(candidate + 4)) {
        publish(Primos(candidate, candidate + 4, count))
        count += 1
        this.setProgress(count * 100 / n)
      }
      candidate += 2
    }
  }

  override def doInBackground(): Unit = {
    this.setProgress(0)
    calcularCousinPrimos(n)
  }

  override def done(): Unit = {
    try {
      if (!this.isCancelled) {
        panel.updateCousinMessage("calculando primos cousin.. terminado")
      }
    } catch {
      case _: CancellationException => // No cambiar el mensaje si fue cancelado
    }
  }

  override def process(chunks: util.List[Primos]): Unit = {
    panel.addCousinPrimos(chunks)
  }
}

// Worker para números primos sexy (distancia 6)
class SexyWorker(n: Int, panel: Panel) extends SwingWorker[Unit, Primos] {

  def esPrimo(n: Int): Boolean = {
    def loop(div: Int): Boolean = {
      if (div * div > n) true
      else if (n % div == 0) false
      else loop(div + 1)
    }
    if (n <= 1) false
    else if (n == 2) true
    else loop(2)
  }

  def calcularSexyPrimos(n: Int): Unit = {
    var count = 0
    var candidate = 5 // Empezamos desde 5 para tener el primer par (5,11)

    while (count < n && !this.isCancelled) {
      if (esPrimo(candidate) && esPrimo(candidate + 6)) {
        publish(Primos(candidate, candidate + 6, count))
        count += 1
        this.setProgress(count * 100 / n)
      }
      candidate += 2
    }
  }

  override def doInBackground(): Unit = {
    this.setProgress(0)
    calcularSexyPrimos(n)
  }

  override def done(): Unit = {
    try {
      if (!this.isCancelled) {
        panel.updateSexyMessage("calculando primos sexy.. terminado")
      }
    } catch {
      case _: CancellationException => // No cambiar el mensaje si fue cancelado
    }
  }

  override def process(chunks: util.List[Primos]): Unit = {
    panel.addSexyPrimos(chunks)
  }
}

// Controlador para gestionar eventos
class Controlador(panel: Panel) extends ActionListener, PropertyChangeListener {

  private var twinWorker: TwinWorker = null
  private var cousinWorker: CousinWorker = null
  private var sexyWorker: SexyWorker = null

  override def actionPerformed(e: ActionEvent): Unit = {
    e.getActionCommand match {
      case "CANCEL" =>
        var canceladas = false
        if (twinWorker != null) {
          twinWorker.cancel(true)
          canceladas = true
        }
        if (cousinWorker != null) {
          cousinWorker.cancel(true)
          canceladas = true
        }
        if (sexyWorker != null) {
          sexyWorker.cancel(true)
          canceladas = true
        }
        if (canceladas) {
          panel.updateStatusMessage("Tareas canceladas")
        }

      case "NUMBER1" => // Twin primos
        try {
          val n = panel.getTwinNumber()
          panel.clearTwinArea()
          panel.setTwinProgress(0)
          panel.updateTwinMessage("calculando primos twin..")
          twinWorker = new TwinWorker(n, panel)
          twinWorker.addPropertyChangeListener(this)
          twinWorker.execute()
        } catch {
          case _: NumberFormatException => panel.updateStatusMessage("Número twin incorrecto")
        }

      case "NUMBER2" => // Cousin primos
        try {
          val n = panel.getCousinNumber()
          panel.clearCousinArea()
          panel.setCousinProgress(0)
          panel.updateCousinMessage("calculando primos cousin..")
          cousinWorker = new CousinWorker(n, panel)
          cousinWorker.addPropertyChangeListener(this)
          cousinWorker.execute()
        } catch {
          case _: NumberFormatException => panel.updateStatusMessage("Número cousin incorrecto")
        }

      case "NUMBER3" => // Sexy primos
        try {
          val n = panel.getSexyNumber()
          panel.clearSexyArea()
          panel.setSexyProgress(0)
          panel.updateSexyMessage("calculando primos sexy..")
          sexyWorker = new SexyWorker(n, panel)
          sexyWorker.addPropertyChangeListener(this)
          sexyWorker.execute()
        } catch {
          case _: NumberFormatException => panel.updateStatusMessage("Número sexy incorrecto")
        }
    }
  }

  override def propertyChange(evt: PropertyChangeEvent): Unit = {
    if (evt.getPropertyName.equals("progress")) {
      val progress = evt.getNewValue.toString.toInt
      val source = evt.getSource

      source match {
        case _: TwinWorker => panel.setTwinProgress(progress)
        case _: CousinWorker => panel.setCousinProgress(progress)
        case _: SexyWorker => panel.setSexyProgress(progress)
      }
    }
  }
}

// Panel principal de la GUI
class Panel extends JPanel {
  val TWIN_FIELD = "NUMBER1"
  val COUSIN_FIELD = "NUMBER2"
  val SEXY_FIELD = "NUMBER3"
  val CANCEL_BUTTON = "CANCEL"

  // Etiquetas y TextFields
  private val twinLabel = new JLabel("cuántos de primos twin quieres?")
  private val twinField = new JTextField(5)
  private val cousinLabel = new JLabel("cuántos de primos cousin quieres?")
  private val cousinField = new JTextField(5)
  private val sexyLabel = new JLabel("cuántos de primos sexy quieres?")
  private val sexyField = new JTextField(5)

  private val statusMessage = new JLabel("GUI creada")

  private val twinArea = new JTextArea(15, 40)
  private val cousinArea = new JTextArea(15, 40)
  private val sexyArea = new JTextArea(15, 40)

  private val twinScroll = new JScrollPane(twinArea)
  private val cousinScroll = new JScrollPane(cousinArea)
  private val sexyScroll = new JScrollPane(sexyArea)

  private val twinMsg = new JLabel("Área Twin creada")
  private val cousinMsg = new JLabel("Área Cousin creada")
  private val sexyMsg = new JLabel("Área Sexy creada")

  private val cancelButton = new JButton("Cancelar")

  private val progressTwin = new JProgressBar(0, 100)
  private val progressCousin = new JProgressBar(0, 100)
  private val progressSexy = new JProgressBar(0, 100)

  init()

  private def init(): Unit = {
    this.setLayout(new BorderLayout())

    val northPanel = new JPanel()
    northPanel.add(cancelButton)

    val centerPanel = new JPanel(new GridLayout(1, 3))

    // Configurar barras de progreso
    progressTwin.setValue(0)
    progressTwin.setStringPainted(true)
    progressCousin.setValue(0)
    progressCousin.setStringPainted(true)
    progressSexy.setValue(0)
    progressSexy.setStringPainted(true)

    // Panel Twin
    val twinTop = new JPanel()
    twinTop.add(twinLabel)
    twinTop.add(twinField)

    val twinPanel = new JPanel(new BorderLayout())
    twinPanel.add(twinTop, BorderLayout.NORTH)
    twinPanel.add(twinScroll, BorderLayout.CENTER)

    val twinBottom = new JPanel()
    twinBottom.add(twinMsg)
    twinBottom.add(progressTwin)
    twinPanel.add(twinBottom, BorderLayout.SOUTH)

    // Panel Cousin
    val cousinTop = new JPanel()
    cousinTop.add(cousinLabel)
    cousinTop.add(cousinField)

    val cousinPanel = new JPanel(new BorderLayout())
    cousinPanel.add(cousinTop, BorderLayout.NORTH)
    cousinPanel.add(cousinScroll, BorderLayout.CENTER)

    val cousinBottom = new JPanel()
    cousinBottom.add(cousinMsg)
    cousinBottom.add(progressCousin)
    cousinPanel.add(cousinBottom, BorderLayout.SOUTH)

    // Panel Sexy
    val sexyTop = new JPanel()
    sexyTop.add(sexyLabel)
    sexyTop.add(sexyField)

    val sexyPanel = new JPanel(new BorderLayout())
    sexyPanel.add(sexyTop, BorderLayout.NORTH)
    sexyPanel.add(sexyScroll, BorderLayout.CENTER)

    val sexyBottom = new JPanel()
    sexyBottom.add(sexyMsg)
    sexyBottom.add(progressSexy)
    sexyPanel.add(sexyBottom, BorderLayout.SOUTH)

    centerPanel.add(twinPanel)
    centerPanel.add(cousinPanel)
    centerPanel.add(sexyPanel)

    this.add(northPanel, BorderLayout.NORTH)
    this.add(centerPanel, BorderLayout.CENTER)
    this.add(statusMessage, BorderLayout.SOUTH)
  }

  def setControlador(ctr: ActionListener): Unit = {
    twinField.setActionCommand(TWIN_FIELD)
    twinField.addActionListener(ctr)

    cousinField.setActionCommand(COUSIN_FIELD)
    cousinField.addActionListener(ctr)

    sexyField.setActionCommand(SEXY_FIELD)
    sexyField.addActionListener(ctr)

    cancelButton.setActionCommand(CANCEL_BUTTON)
    cancelButton.addActionListener(ctr)
  }

  // Métodos para obtener números de los campos
  def getTwinNumber(): Int = Integer.parseInt(twinField.getText)
  def getCousinNumber(): Int = Integer.parseInt(cousinField.getText)
  def getSexyNumber(): Int = Integer.parseInt(sexyField.getText)

  // Métodos para limpiar áreas de texto
  def clearTwinArea(): Unit = twinArea.setText("")
  def clearCousinArea(): Unit = cousinArea.setText("")
  def clearSexyArea(): Unit = sexyArea.setText("")

  // Métodos para actualizar barras de progreso
  def setTwinProgress(n: Int): Unit = progressTwin.setValue(n)
  def setCousinProgress(n: Int): Unit = progressCousin.setValue(n)
  def setSexyProgress(n: Int): Unit = progressSexy.setValue(n)

  // Métodos para actualizar mensajes
  def updateStatusMessage(str: String): Unit = statusMessage.setText(str)
  def updateTwinMessage(str: String): Unit = twinMsg.setText(str)
  def updateCousinMessage(str: String): Unit = cousinMsg.setText(str)
  def updateSexyMessage(str: String): Unit = sexyMsg.setText(str)

  // Métodos para añadir primos a las áreas de texto
  def addTwinPrimos(lista: java.util.List[Primos]): Unit = {
    for (i <- 0 until lista.size()) {
      val primo = lista.get(i)
      twinArea.append(s"${primo.posicion}:(${primo.a},${primo.b})  ")
      if ((primo.posicion + 1) % 5 == 0) twinArea.append("\n")
    }
  }

  def addCousinPrimos(lista: java.util.List[Primos]): Unit = {
    for (i <- 0 until lista.size()) {
      val primo = lista.get(i)
      cousinArea.append(s"${primo.posicion}:(${primo.a},${primo.b})  ")
      if ((primo.posicion + 1) % 5 == 0) cousinArea.append("\n")
    }
  }

  def addSexyPrimos(lista: java.util.List[Primos]): Unit = {
    for (i <- 0 until lista.size()) {
      val primo = lista.get(i)
      sexyArea.append(s"${primo.posicion}:(${primo.a},${primo.b})  ")
      if ((primo.posicion + 1) % 5 == 0) sexyArea.append("\n")
    }
  }
}

object Main {
  def crearGUI(ventana: JFrame): Unit = {
    val panel = new Panel
    val controlador = new Controlador(panel)
    panel.setControlador(controlador)
    ventana.setContentPane(panel)
    ventana.pack()
    ventana.setVisible(true)
    ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  }

  def main(args: Array[String]): Unit = {
    SwingUtilities.invokeLater(
      new Runnable {
        override def run(): Unit = {
          val ventana = new JFrame("Números primos")
          crearGUI(ventana)
        }
      }
    )
  }
}