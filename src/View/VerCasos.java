package View;

import Controller.CasoController;
import Model.Entities.Caso;
import Model.Entities.Estado;
import Model.Service.CasoService;


import java.util.List;
import javax.swing.*;
import java.awt.*;

public class VerCasos extends JFrame {
    CasoService casoService = new  CasoService();
    CasoController casoController = new  CasoController();
    private static final Color MAIN_BG_COLOR = new Color(18, 18, 20);
    private static final Color PANEL_BG_COLOR = new Color(26, 26, 30);
    private static final Color BORDER_COLOR = new Color(28, 28, 34);
    private static final Color TEXT_COLOR = new Color(170, 170, 175);
    private static final Color TITLE_COLOR = new Color(245, 240, 230);
    public VerCasos() {
        setTitle("Ver Casos");

        setSize(600, 650);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Layout Ventana
        setLayout(new BorderLayout());

        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();

        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void initComponents() {


        //Titulo ventana principal
        JLabel titulo = new JLabel("CASOS DISPONIBLES", SwingConstants.CENTER);

        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(new Font("Georgia", Font.BOLD, 26));

        titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));

        add(titulo, BorderLayout.NORTH);


        //Panel de casos
        List<Caso> casosNoResueltos = casoService.obtenerCasosporEstado(Estado.NORESUELTO);
        List<Caso> casosPendientes = casoService.obtenerCasosporEstado(Estado.PENDIENTE);
        List<Caso> casosListaDesordenada = casosNoResueltos;
        casosListaDesordenada.addAll(casosPendientes);

        //Lo ordenamos por id
        List<Caso> casos = casosListaDesordenada.stream()
                .sorted()
                .toList();

        JPanel panelCasos = new JPanel();

        panelCasos.setBackground(MAIN_BG_COLOR);

        panelCasos.setLayout(new GridLayout(0, 1, 0, 20));

        panelCasos.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));


        //Cargar casos
        for (Caso caso : casos) {
            panelCasos.add(mostrarCasos(caso));
        }

        //ScrollPane
        JScrollPane scroll = new JScrollPane(panelCasos);

        scroll.setBorder(null);
        scroll.setBackground(MAIN_BG_COLOR);
        scroll.getViewport().setBackground(MAIN_BG_COLOR);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        //Panel inferior para boton salir de la ventana principal
        JPanel panelInferiorVentana = new JPanel(new BorderLayout());
        panelInferiorVentana.setBackground(MAIN_BG_COLOR);
        panelInferiorVentana.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        //Boton salir en la esquina izquierda abajo igual que en VentanaNotas
        JButton btnSalirVentana = new JButton("← Salir");
        btnSalirVentana.setBackground(PANEL_BG_COLOR);
        btnSalirVentana.setForeground(TEXT_COLOR);
        btnSalirVentana.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnSalirVentana.setFocusPainted(false);
        btnSalirVentana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalirVentana.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnSalirVentana.setPreferredSize(new Dimension(130, 40));
        btnSalirVentana.addActionListener(e -> dispose()); // Cambiar por System.exit(0) si cierra el programa entero

        panelInferiorVentana.add(btnSalirVentana, BorderLayout.WEST);
        add(panelInferiorVentana, BorderLayout.SOUTH);

    }

    //Funcion para mostrar los casos
    public JPanel mostrarCasos(Caso caso) {

        //Panel casos
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout(0, 12));

        panel.setBackground(PANEL_BG_COLOR);


        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));


        //Titulo Caso
        JLabel tituloCaso = new JLabel("Caso #" + caso.getId_caso() + ": " + caso.getNombre(), SwingConstants.LEFT);

        tituloCaso.setForeground(TITLE_COLOR);
        tituloCaso.setFont(new Font("Monospaced", Font.BOLD, 16));

        panel.add(tituloCaso, BorderLayout.NORTH);


        //Descripcion Caso
        JLabel descripcion = new JLabel(caso.getDescripcion(), SwingConstants.LEFT);

        descripcion.setForeground(TEXT_COLOR);

        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panel.add(descripcion, BorderLayout.CENTER);


        //Panel inferior

        JPanel panelInferior = new JPanel(new BorderLayout());

        panelInferior.setOpaque(false);

        //Lable sospechosos
        JLabel sospechosos = new JLabel("Sospechosos: " + caso.getSospechosos().size());

        sospechosos.setForeground(TEXT_COLOR);
        sospechosos.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panelInferior.add(sospechosos, BorderLayout.WEST);

        //Boton Aceptar
        JButton btnAceptar = new JButton("Aceptar");

        btnAceptar.setFocusPainted(false);

        btnAceptar.setBackground(MAIN_BG_COLOR);

        btnAceptar.setForeground(TITLE_COLOR);
        btnAceptar.setFont(new Font("Monospaced", Font.BOLD, 13));
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btnAceptar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        //Listener boton aceptar
        btnAceptar.addActionListener(e->{
            System.out.println(caso);
            casoController.seleccionarCaso(caso.getId_caso())
            ;});

        panelInferior.add(btnAceptar, BorderLayout.EAST);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    static void main() {
        java.awt.EventQueue.invokeLater(() -> {
            new VerCasos().setVisible(true);
        });
    }
}