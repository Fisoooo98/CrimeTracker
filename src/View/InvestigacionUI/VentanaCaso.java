package View.InvestigacionUI;

import Controller.CasoController;
import Controller.JuegoController;
import Model.Entities.Caso;
import Model.Service.CasoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaCaso extends JFrame {
    private Caso casoActual;
    private JuegoController juegoController = new JuegoController();
    private static final Color BG_DARK = new Color(8, 8, 10);
    private static final Color PANEL_BG = new Color(15, 15, 18);
    private static final Color CARD_BG = new Color(18, 18, 22);
    private static final Color BORDER_COLOR = new Color(30, 30, 35);

    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_MUTED = new Color(200, 200, 200);
    private static final Color TEXT_GOLD = new Color(240, 225, 200);

    private static final Color BTN_BLUE = new Color(20, 20, 24);
    private static final Color BTN_GREEN = new Color(20, 20, 24);
    private static final Color BTN_PURPLE = new Color(20, 20, 24);
    private static final Color BTN_YELLOW = new Color(20, 20, 24);
    private static final Color BTN_RED = new Color(20, 20, 24);

    public VentanaCaso(Caso caso) {
        this.casoActual = caso;
        setTitle("Ver Casos");
        setSize(1200, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        initComponents();
        setLocationRelativeTo(null);
    }

    public void initComponents(){
        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    public void actualizarPantalla(Caso casoActualizado) {
        this.casoActual = casoActualizado;

        getContentPane().removeAll();

        initComponents();

        revalidate();

        repaint();
    }

    public JPanel crearPanelIzquierdo(){
        JPanel lateral = new JPanel();
        lateral.setBackground(PANEL_BG);
        lateral.setPreferredSize(new Dimension(280, 0));

        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR), new EmptyBorder(30, 24, 25, 24)));

        //Panel lateral norte
        JPanel lateralNorte = new JPanel();
        lateralNorte.setLayout(new BoxLayout(lateralNorte, BoxLayout.Y_AXIS));
        lateralNorte.setBackground(PANEL_BG);
        lateralNorte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTituloSeccion = new JLabel("CASO SELECCIONADO");
        lblTituloSeccion.setForeground(TEXT_GOLD);
        lblTituloSeccion.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTituloSeccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralNorte.add(lblTituloSeccion);

        lateralNorte.add(Box.createVerticalStrut(8));

        JLabel lblTituloCaso = new JLabel("CASO: #" + casoActual.getId_caso());
        lblTituloCaso.setForeground(TEXT_GOLD);
        lblTituloCaso.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTituloCaso.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralNorte.add(lblTituloCaso);

        lateralNorte.add(Box.createVerticalStrut(4));

        JLabel lblNombreCaso = new JLabel(casoActual.getNombre());
        lblNombreCaso.setForeground(TEXT_WHITE);
        lblNombreCaso.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblNombreCaso.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralNorte.add(lblNombreCaso);


        //Panel lateral Centro
        JPanel lateralCentro = new JPanel();
        lateralCentro.setLayout(new BoxLayout(lateralCentro, BoxLayout.Y_AXIS));
        lateralCentro.setBackground(PANEL_BG);
        lateralCentro.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Descripción");
        lblDescripcion.setForeground(TEXT_WHITE);
        lblDescripcion.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralCentro.add(lblDescripcion);

        lateralCentro.add(Box.createVerticalStrut(8));

        JTextArea descripcion = new JTextArea(casoActual.getDescripcion());
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setBackground(PANEL_BG);
        descripcion.setForeground(TEXT_MUTED);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descripcion.setOpaque(false);
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        descripcion.setMaximumSize(new Dimension(230, 120));
        lateralCentro.add(descripcion);


        //Panel lateral Sur
        JPanel lateralSur = new JPanel();
        lateralSur.setLayout(new BoxLayout(lateralSur, BoxLayout.Y_AXIS));
        lateralSur.setBackground(PANEL_BG);
        lateralSur.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSospechosos = new JLabel("Sospechosos: " + casoActual.getSospechosos().size());
        lblSospechosos.setForeground(TEXT_WHITE);
        lblSospechosos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSospechosos.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralSur.add(lblSospechosos);

        lateralSur.add(Box.createVerticalStrut(6));

        JLabel lblEvidencias = new JLabel("Evidencias: " + casoActual.getEvidencias().size());
        lblEvidencias.setForeground(TEXT_WHITE);
        lblEvidencias.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEvidencias.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralSur.add(lblEvidencias);

        lateralSur.add(Box.createVerticalStrut(6));

        JLabel lblPistas = new JLabel("Pistas: " + casoActual.getPistas().size());
        lblPistas.setForeground(TEXT_WHITE);
        lblPistas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPistas.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralSur.add(lblPistas);

        lateralSur.add(Box.createVerticalStrut(6));

        // Información de dificultad agregada aquí
        JLabel lblDificultad = new JLabel("Dificultad: " + casoActual.getDificultad());
        lblDificultad.setForeground(TEXT_WHITE);
        lblDificultad.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateralSur.add(lblDificultad);


        lateral.add(lateralNorte);
        lateral.add(Box.createVerticalStrut(30));
        lateral.add(lateralCentro);
        lateral.add(Box.createVerticalStrut(30));
        lateral.add(lateralSur);

        lateral.add(Box.createVerticalGlue());

        return lateral;
    }

    public JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(PANEL_BG);

        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        //Boton Salir
        JButton btnSalir = new JButton("← Salir");

        btnSalir.setBackground(new Color(20,20,24));

        btnSalir.setForeground(TEXT_WHITE);

        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnSalir.setBorderPainted(false);

        btnSalir.setFocusPainted(false);


        btnSalir.setPreferredSize(new Dimension(120, 40));

       btnSalir.addActionListener(e->dispose());

        panelInferior.add(btnSalir, BorderLayout.WEST);

        return panelInferior;

    }
    public JPanel crearPanelCentral(){
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBackground(BG_DARK);

        //Titulo
        JLabel Titulo = new JLabel("Investigar Casos",SwingConstants.CENTER);
        Titulo.setForeground(TEXT_GOLD);
        Titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        Titulo.setBackground(PANEL_BG);
        Titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panelCentral.add(Titulo,BorderLayout.NORTH);

        //Panel acciones
        JPanel panelAcciones = new JPanel();
        panelAcciones.setBackground(BG_DARK);

        panelAcciones.setBorder(new EmptyBorder(40, 30, 40, 30));

        panelAcciones.setLayout(new GridLayout(1, 5, 20, 0));

        panelAcciones.add(
                crearCard(
                        "EVIDENCIAS Y PISTAS",
                        "Consulta todas las pistas obtenidas durante la investigación.",
                        BTN_BLUE,
                        e -> juegoController.AbrirPistas(this)
                )
        );

        panelAcciones.add(
                crearCard(
                        "INTERROGAR",
                        "Haz preguntas a los sospechosos para obtener información.",
                        BTN_GREEN,
                        e-> juegoController.AbrirInterrogatorio(this)
                )
        );


        panelAcciones.add(
                crearCard(
                        "NOTAS",
                        "Guarda observaciones e hipótesis mientras investigas.",
                        BTN_YELLOW,
                        e-> juegoController.AbrirNotas()
                )
        );

        panelCentral.add(panelAcciones,BorderLayout.CENTER);

        return panelCentral;
    }


    public JPanel crearCard(String titulo, String descripcion, Color colorBoton, ActionListener listener) {

        JPanel card = new JPanel();

        card.setBackground(CARD_BG);

        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1), new EmptyBorder(20, 20, 20, 20)));

        card.setLayout(new BorderLayout(0, 15));

        //Titulo

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);

        lblTitulo.setForeground(TEXT_WHITE);

        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        card.add(lblTitulo, BorderLayout.NORTH);

        //Descripcion

        JTextArea txtDescripcion = new JTextArea(descripcion);

        txtDescripcion.setEditable(false);

        txtDescripcion.setOpaque(false);

        txtDescripcion.setLineWrap(true);

        txtDescripcion.setWrapStyleWord(true);

        txtDescripcion.setForeground(TEXT_MUTED);

        txtDescripcion.setFont(new Font("SansSerif",Font.PLAIN, 12));

        card.add(txtDescripcion, BorderLayout.CENTER);

        // Boton
        JButton btnAbrir = new JButton("Abrir");

        btnAbrir.setBackground(colorBoton);

        btnAbrir.setForeground(TEXT_WHITE);

        btnAbrir.setFocusPainted(false);

        btnAbrir.addActionListener(listener);

        card.add(btnAbrir, BorderLayout.SOUTH);

        return card;
    }

    public Caso getCasoActual() {
        return casoActual;
    }
}