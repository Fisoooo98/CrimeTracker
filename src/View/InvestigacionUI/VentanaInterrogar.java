package View.InvestigacionUI;

import Controller.JuegoController;
import Model.Entities.Caso;
import Model.Entities.Sospechoso;
import Model.Service.CasoService;
import Model.Service.JuegoService;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaInterrogar extends JFrame {
    private Caso caso;
    private VentanaCaso ventanaCaso;
    private JLabel etiquetaTextoDialogo;
    private Timer timerActual;
    private JuegoService juegoService = new JuegoService();
    private CasoService casoService = new CasoService();
    private Sospechoso sospechosoActual;
    private JuegoController juegoController;
    private JPanel panelPreguntasActual;
    // Colores de la interfaz
    private static final Color MAIN_BG_COLOR = new Color(24, 24, 24);
    private static final Color PANEL_BG_COLOR = new Color(36, 36, 36);
    private static final Color BORDER_COLOR = new Color(50, 50, 50);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color TITLE_COLOR = new Color(200, 210, 220);

    public VentanaInterrogar(Caso caso,VentanaCaso ventanaCaso,JuegoController juegoController) {
        this.caso = caso;
        this.ventanaCaso = ventanaCaso;
        this.juegoController = juegoController;
        setTitle("Interrogar Sospechosos");
        setSize(1400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();
        setLocationRelativeTo(null);

        actualizarTextoDialogo("Selecciona a un sospechoso para comenzar el interrogatorio...");
    }

    public void initComponents() {
        add(CrearLogDialogo(), BorderLayout.SOUTH);

        add(crearPanelSospechosos(caso.getSospechosos()), BorderLayout.CENTER);

        add(crearTitulo(), BorderLayout.NORTH);

    }


    public JPanel crearTitulo() {
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelTitulo.setBackground(MAIN_BG_COLOR);

        JLabel lblTitulo = new JLabel("INTERROGAR SOSPECHOSOS");
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 28));
        lblTitulo.setForeground(TITLE_COLOR);

        panelTitulo.add(lblTitulo);
        return panelTitulo;
    }

    public JPanel crearPanelSospechosos(List<Sospechoso> listaSospechosos) {
        JPanel panelContenedor = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 50));
        panelContenedor.setBackground(MAIN_BG_COLOR);

        ImageIcon iconoSilueta = generarIconoSospechoso(160, 200);

        for (Sospechoso sospechoso : listaSospechosos) {
            JButton botonSospechoso = new JButton();
            botonSospechoso.setLayout(new BorderLayout(0, 10));

            //Boton que para seleccionar al sospechoso
            botonSospechoso.setPreferredSize(new Dimension(220, 280));
            botonSospechoso.setBackground(PANEL_BG_COLOR);
            botonSospechoso.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 3));
            botonSospechoso.setFocusPainted(false);
            botonSospechoso.setCursor(new Cursor(Cursor.HAND_CURSOR));

            //Nombre del sospechoso
            JLabel lblNombre = new JLabel(sospechoso.getNombre(), SwingConstants.CENTER);
            lblNombre.setFont(new Font("Monospaced", Font.BOLD, 18)); // Fuente más grande
            lblNombre.setForeground(TEXT_COLOR);
            lblNombre.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            //Silueta
            JLabel lblIcono = new JLabel(iconoSilueta, SwingConstants.CENTER);
            botonSospechoso.add(lblNombre, BorderLayout.NORTH);
            botonSospechoso.add(lblIcono, BorderLayout.CENTER);

            //Listener para acceder al sospechoso
            final Sospechoso sospechosoActual = sospechoso;
            botonSospechoso.addActionListener(e -> {
                actualizarTextoDialogo("Estás interrogando a " + sospechosoActual.getNombre() + "... ¿Qué tiene que ocultar?");

                if (this.panelPreguntasActual != null) {
                    remove(this.panelPreguntasActual);
                }

                this.panelPreguntasActual = panelPreguntas(sospechosoActual.getId_sospechoso(), caso.getId_caso());

                add(this.panelPreguntasActual, BorderLayout.EAST);

                this.sospechosoActual = sospechosoActual;

                revalidate();
                repaint();
            });


            panelContenedor.add(botonSospechoso);
        }

        return panelContenedor;
    }

    private static ImageIcon generarIconoSospechoso(int ancho, int alto) {
        Image img = new java.awt.image.BufferedImage(ancho, alto, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Cuerpo ---
        g2.setColor(Color.BLACK);
        int cuerpoAncho = (int) (ancho * 0.85);
        int cuerpoAlto = (int) (alto * 0.55);
        int cuerpoX = (ancho - cuerpoAncho) / 2;
        int cuerpoY = alto - cuerpoAlto - 10;
        g2.fill(new RoundRectangle2D.Float(cuerpoX, cuerpoY, cuerpoAncho, cuerpoAlto, 50, 50));

        // --- Cabeza ---
        int cabezaDiametro = (int) (ancho * 0.55);
        int cabezaX = (ancho - cabezaDiametro) / 2;
        int cabezaY = (int) (cuerpoY - cabezaDiametro * 0.82);
        g2.fill(new Ellipse2D.Float(cabezaX, cabezaY, cabezaDiametro, cabezaDiametro));

        // --- Ojos Maliciosos (Escalados proporcionalmente al nuevo tamaño) ---
        g2.setColor(Color.WHITE);
        int centroCabezaX = cabezaX + (cabezaDiametro / 2);
        int centroCabezaY = cabezaY + (cabezaDiametro / 2);

        int[] xOjoIzq = {centroCabezaX - 26, centroCabezaX - 6, centroCabezaX - 20};
        int[] yOjoIzq = {centroCabezaY - 8, centroCabezaY - 5, centroCabezaY + 4};
        g2.fillPolygon(xOjoIzq, yOjoIzq, 3);

        int[] xOjoDer = {centroCabezaX + 26, centroCabezaX + 6, centroCabezaX + 20};
        int[] yOjoDer = {centroCabezaY - 8, centroCabezaY - 5, centroCabezaY + 4};
        g2.fillPolygon(xOjoDer, yOjoDer, 3);

        g2.dispose();
        return new ImageIcon(img);
    }


    public JPanel CrearLogDialogo() {
        JPanel panelLog = new JPanel();
        panelLog.setLayout(new BorderLayout());
        panelLog.setBackground(PANEL_BG_COLOR);
        panelLog.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 4));
        panelLog.setPreferredSize(new Dimension(1100, 120));

        etiquetaTextoDialogo = new JLabel("<html></html>");
        etiquetaTextoDialogo.setForeground(TITLE_COLOR);
        etiquetaTextoDialogo.setFont(new Font("Monospaced", Font.BOLD, 18));
        etiquetaTextoDialogo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 35));
        panelBotonesAccion.setBackground(PANEL_BG_COLOR);

        //Botón Acusar
        JButton botonAcusar = new JButton("Acusar");
        botonAcusar.setFont(new Font("Monospaced", Font.BOLD, 14));
        botonAcusar.setBackground(MAIN_BG_COLOR);
        botonAcusar.setForeground(TEXT_COLOR);
        botonAcusar.setFocusPainted(false);
        botonAcusar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAcusar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        botonAcusar.addActionListener(e -> {
            if (this.sospechosoActual == null) {
                actualizarTextoDialogo("Tienes que acusar a alguien");
            }else{
                juegoController.acusar(this.sospechosoActual.getId_sospechoso(),this);
            }
        });

        //Botón Salir
        JButton botonSalir = new JButton("Salir");
        botonSalir.setFont(new Font("Monospaced", Font.BOLD, 14));
        botonSalir.setBackground(MAIN_BG_COLOR);
        botonSalir.setForeground(TEXT_COLOR);
        botonSalir.setFocusPainted(false);
        botonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonSalir.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        botonSalir.addActionListener(
                e ->{
                    Caso casoActualizado = casoService.obtenerCasoActivo();
                    ventanaCaso.actualizarPantalla(casoActualizado);
                    dispose();
                }
        );
        panelBotonesAccion.add(botonAcusar);
        panelBotonesAccion.add(botonSalir);

        panelLog.add(etiquetaTextoDialogo, BorderLayout.CENTER);
        panelLog.add(panelBotonesAccion, BorderLayout.EAST);

        return panelLog;
    }

    public void actualizarTextoDialogo(String texto) {

        if (timerActual != null && timerActual.isRunning()) {
            timerActual.stop();
        }

        timerActual = new Timer(25, new ActionListener() {
            private int index = 0;
            private final StringBuilder textoParcial = new StringBuilder();

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < texto.length()) {
                    textoParcial.append(texto.charAt(index));
                    etiquetaTextoDialogo.setText("<html>" + textoParcial + "</html>");
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timerActual.start();
    }
    public JPanel panelPreguntas(int id_sospechoso, int idcaso) {

        List<String> preguntas = juegoService.InterrogarSospechoso(id_sospechoso, idcaso);


        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(MAIN_BG_COLOR);

        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));


        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 0, 5));
        panelBotones.setBackground(MAIN_BG_COLOR);

        for (String pregunta : preguntas) {
            JButton botonPregunta = new JButton(pregunta);
            botonPregunta.setHorizontalAlignment(SwingConstants.LEFT);
            botonPregunta.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente más pequeña (12)
            botonPregunta.setBackground(PANEL_BG_COLOR);
            botonPregunta.setForeground(TEXT_COLOR);
            botonPregunta.setFocusPainted(false);
            botonPregunta.setCursor(new Cursor(Cursor.HAND_CURSOR));

            botonPregunta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));

            botonPregunta.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    botonPregunta.setBackground(new Color(65, 65, 70));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    botonPregunta.setBackground(PANEL_BG_COLOR);
                }
            });

            //Listener
            botonPregunta.putClientProperty("preguntaTexto", pregunta);
            botonPregunta.addActionListener(e -> {
                JButton btn = (JButton) e.getSource();
                juegoController.interrogar(btn.getText(),id_sospechoso,this);
            });


            panelBotones.add(botonPregunta);
        }

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        return panelPrincipal;
    }

    public void mostrarDialogo(String texto) {
        JDialog dialogo = new JDialog(this, "Notificación", true);
        dialogo.setUndecorated(true);
        dialogo.setSize(400, 180);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(PANEL_BG_COLOR);
        panelFondo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));

        JLabel lblMensaje = new JLabel("<html><body style='text-align: center; width: 300px;'>" + texto + "</body></html>", SwingConstants.CENTER);
        lblMensaje.setForeground(TITLE_COLOR);
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelFondo.add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnEntendido = new JButton("Entendido");
        btnEntendido.setBackground(MAIN_BG_COLOR);
        btnEntendido.setForeground(TEXT_COLOR);
        btnEntendido.setFont(new Font("Monospaced", Font.BOLD, 13));
        btnEntendido.setFocusPainted(false);
        btnEntendido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntendido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 25, 6, 25)
        ));
        btnEntendido.addActionListener(e -> dialogo.dispose());
        panelBoton.add(btnEntendido);

        panelFondo.add(panelBoton, BorderLayout.SOUTH);
        dialogo.add(panelFondo);
        dialogo.setVisible(true);
    }

    public Caso getCaso() {
        return caso;
    }

    public Sospechoso getSospechosoActual() {
        return sospechosoActual;
    }
}