package View.InvestigacionUI;

import Controller.JuegoController;
import Model.Entities.Caso;
import Model.Service.CasoService;
import Model.Service.JuegoService;

import javax.swing.*;
import java.awt.*;

public class VentanaNotas extends JFrame {
    private Caso casoActual;
    JuegoController juegoController = new JuegoController();
    JuegoService juegoService = new JuegoService();
    private static final Color MAIN_BG_COLOR = new Color(18, 18, 20);
    private static final Color PANEL_BG_COLOR = new Color(26, 26, 30);
    private static final Color BORDER_COLOR = new Color(28, 28, 34);
    private static final Color TEXT_COLOR = new Color(170, 170, 175);
    private static final Color TITLE_COLOR = new Color(245, 240, 230);

    public VentanaNotas(Caso caso) {
        this.casoActual = caso;
        this.setTitle("Notas de Pistas");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 650);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();

        setLocationRelativeTo(null);
    }

    private void initComponents() {
        //Titulo Principal
        JLabel Titulo = new JLabel("NOTAS GUARDADAS", SwingConstants.CENTER);
        Titulo.setForeground(TITLE_COLOR);
        Titulo.setFont(new Font("Georgia", Font.BOLD, 26));
        Titulo.setBackground(MAIN_BG_COLOR);
        Titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(Titulo, BorderLayout.NORTH);

        //PanelCentro
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(MAIN_BG_COLOR);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //TextArea
        JTextArea blocNotas = new JTextArea();
        blocNotas.setFont(new Font("Monospaced", Font.PLAIN, 15));
        blocNotas.setForeground(TEXT_COLOR);
        blocNotas.setEditable(true);
        blocNotas.setBackground(PANEL_BG_COLOR);
        blocNotas.setCaretColor(Color.WHITE);
        blocNotas.setLineWrap(true);
        blocNotas.setWrapStyleWord(true);
        blocNotas.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cargarNota(blocNotas);

        JScrollPane scrollNotas = new JScrollPane(blocNotas);
        scrollNotas.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        scrollNotas.setBackground(PANEL_BG_COLOR);
        scrollNotas.getViewport().setBackground(PANEL_BG_COLOR);

        panelCentro.add(scrollNotas, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        //Panel Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(MAIN_BG_COLOR);
        panelBotones.setLayout(new BorderLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));

        //Boton Salir
        JButton btnSalir = new JButton("← Salir");
        btnSalir.setBackground(PANEL_BG_COLOR);
        btnSalir.setForeground(TEXT_COLOR);
        btnSalir.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnSalir.setPreferredSize(new Dimension(130, 40));
        btnSalir.addActionListener(e->dispose());
        panelBotones.add(btnSalir, BorderLayout.WEST);

        //Boton Guardar
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(PANEL_BG_COLOR);
        btnGuardar.setForeground(TEXT_COLOR);
        btnGuardar.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnGuardar.setPreferredSize(new Dimension(130, 40));
        btnGuardar.addActionListener(e->juegoController.guardarNota(blocNotas.getText(),casoActual));
        panelBotones.add(btnGuardar, BorderLayout.EAST);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarNota(JTextArea blocnotas){
        blocnotas.setText(juegoService.leerNota(casoActual.getId_caso()));
    }

    static void main() {
        CasoService casoService = new CasoService();
        Caso caso = casoService.obtenerCasoPorId(1);
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaNotas(caso).setVisible(true);
        });
    }
}