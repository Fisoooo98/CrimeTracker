package View.InvestigacionUI;

import Model.Entities.Caso;
import Model.Entities.Evidencia;
import Model.Entities.Pista;
import Model.Service.CasoService;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla del juego que sirve para listar y leer todas las pistas y evidencias descubiertas.
 */
public class VentanaPistas extends JFrame {
    private Caso casoactual;
    private CasoService casoService;

    // Colores de la interfaz
    private static final Color MAIN_BG_COLOR = new Color(18, 18, 20);
    private static final Color PANEL_BG_COLOR = new Color(26, 26, 30);
    private static final Color BORDER_COLOR = new Color(28, 28, 34);
    private static final Color TEXT_COLOR = new Color(170, 170, 175);
    private static final Color TITLE_COLOR = new Color(245, 240, 230);

    /**
     * Abre la ventana de pistas y evidencias con un tamaño fijo de 600x750.
     * * @param ventanaCaso La ventana anterior de la que se recupera el caso que está activo.
     */
    public VentanaPistas(VentanaCaso ventanaCaso) {
        this.casoactual = ventanaCaso.getCasoActual();

        setTitle("Pistas Y Evidencias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();

        setLocationRelativeTo(null);
    }

    /**
     * Añade a la ventana el título arriba, las listas con scroll en el centro y el botón para salir abajo.
     */
    private void initComponents() {
        //Titulo
        JLabel Titulo = new JLabel("VER PISTAS Y EVIDENCIAS",SwingConstants.CENTER);
        Titulo.setForeground(TITLE_COLOR);
        Titulo.setFont(new Font("Georgia", Font.BOLD, 26));
        Titulo.setBackground(MAIN_BG_COLOR);
        Titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(Titulo,BorderLayout.NORTH);
        add(generarPanelPrincipal(),BorderLayout.CENTER);

        //Panel inferior para boton salir
        JPanel panelInferiorVentana = new JPanel(new BorderLayout());
        panelInferiorVentana.setBackground(MAIN_BG_COLOR);
        panelInferiorVentana.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        //Boton salir en la esquina izquierda abajo
        JButton btnSalir = new JButton("← Salir");
        btnSalir.setBackground(PANEL_BG_COLOR);
        btnSalir.setForeground(TEXT_COLOR);
        btnSalir.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnSalir.setPreferredSize(new Dimension(130, 40));
        btnSalir.addActionListener(e -> dispose());

        panelInferiorVentana.add(btnSalir, BorderLayout.WEST);
        add(panelInferiorVentana, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel central dividido en dos secciones fijas: una para pistas y otra para evidencias.
     * * @return El panel principal maquetado en formato JPanel.
     */
    public JPanel generarPanelPrincipal(){
        //Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(MAIN_BG_COLOR);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelPrincipal.setLayout(new GridLayout(2,1,0,20));

        //Panel Pistas
        JPanel panelPistas = new JPanel();
        panelPistas.setBackground(PANEL_BG_COLOR);
        panelPistas.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        panelPistas.setLayout(new BorderLayout());

        //Titulo Pistas
        JLabel tituloPistas = new JLabel("PISTAS",SwingConstants.CENTER);
        tituloPistas.setForeground(TITLE_COLOR);
        tituloPistas.setFont(new Font("Monospaced", Font.BOLD, 18));
        tituloPistas.setBackground(PANEL_BG_COLOR);
        tituloPistas.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panelPistas.add(tituloPistas,BorderLayout.NORTH);

        //Panel Mostrar Pistas
        JPanel panelMostrarPistas = new JPanel();
        panelMostrarPistas.setBackground(PANEL_BG_COLOR);
        panelMostrarPistas.setLayout(new GridLayout(0,1,0,15));

        //Añadir Pistas
        for (Pista p : casoactual.getPistas()){
            JLabel labelpistas = new JLabel("<html><body style='text-align: center; width: 350px;'>" + p.getTexto() + "</body></html>", SwingConstants.CENTER);
            labelpistas.setFont(new Font("SansSerif", Font.PLAIN, 14));
            labelpistas.setForeground(TEXT_COLOR);
            panelMostrarPistas.add(labelpistas);
        }

        //ScrollPane
        JScrollPane scrollPistas = new JScrollPane(panelMostrarPistas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPistas.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        scrollPistas.setBackground(PANEL_BG_COLOR);
        scrollPistas.getViewport().setBackground(PANEL_BG_COLOR);
        scrollPistas.getVerticalScrollBar().setUnitIncrement(16);
        panelPistas.add(scrollPistas, BorderLayout.CENTER);

        panelPrincipal.add(panelPistas);


        //Panel Evidencias
        JPanel panelEvidencias = new JPanel();
        panelEvidencias.setBackground(PANEL_BG_COLOR);
        panelEvidencias.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        panelEvidencias.setLayout(new BorderLayout());

        //Titulo Evidencias
        JLabel tituloEvidencias = new JLabel("EVIDENCIAS",SwingConstants.CENTER);
        tituloEvidencias.setForeground(TITLE_COLOR);
        tituloEvidencias.setFont(new Font("Monospaced", Font.BOLD, 18));
        tituloEvidencias.setBackground(PANEL_BG_COLOR);
        tituloEvidencias.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        panelEvidencias.add(tituloEvidencias,BorderLayout.NORTH);

        //Panel Mostrar Evidencias
        JPanel panelMostrarEvidencias = new JPanel();
        panelMostrarEvidencias.setBackground(PANEL_BG_COLOR);
        panelMostrarEvidencias.setLayout(new GridLayout(0,1,0,15));

        //Mostrar Evidencias
        for (Evidencia e : casoactual.getEvidencias()){
            JLabel labelEvidencias = new JLabel("<html><body style='text-align: center; width: 350px;'>" + e.getTexto() + "</body></html>", SwingConstants.CENTER);
            labelEvidencias.setFont(new Font("SansSerif", Font.PLAIN, 14));
            labelEvidencias.setBackground(PANEL_BG_COLOR);
            labelEvidencias.setForeground(TEXT_COLOR);
            panelMostrarEvidencias.add(labelEvidencias);
        }

        //ScrollPane
        JScrollPane scrollEvidencias = new JScrollPane(panelMostrarEvidencias, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollEvidencias.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        scrollEvidencias.setBackground(PANEL_BG_COLOR);
        scrollEvidencias.getViewport().setBackground(PANEL_BG_COLOR);
        scrollEvidencias.getVerticalScrollBar().setUnitIncrement(16);
        panelEvidencias.add(scrollEvidencias, BorderLayout.CENTER);

        panelPrincipal.add(panelEvidencias);
        return panelPrincipal;
    }
}