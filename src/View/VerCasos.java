package View;

import Controller.CasoController;
import Model.Entities.Caso;
import Model.Entities.Dificultad;
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
        System.out.println(casos);

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
            casoController.seleccionarCaso(caso.getId_caso(),this)
            ;});

        panelInferior.add(btnAceptar, BorderLayout.EAST);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    //Panel dificultad
    public void panelDificultad(int idcaso) {
        JDialog dialogo = new JDialog(this, "Seleccionar Dificultad", true);
        dialogo.setUndecorated(true);
        dialogo.setSize(500, 220);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(PANEL_BG_COLOR);
        panelFondo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));

        JLabel lblTitulo = new JLabel("SELECCIONA LA DIFICULTAD", SwingConstants.CENTER);
        lblTitulo.setForeground(TITLE_COLOR);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panelFondo.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel(new GridLayout(1, 3, 15, 0));
        panelOpciones.setOpaque(false);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        String[] dificultades = {"FACIL", "NORMAL", "DIFICIL"};
        String[] descripciones = {
                "<html><center>Más preguntas disponibles y pistas claras.</center></html>",
                "<html><center>Cantidad estándar de preguntas y sospechas equilibradas.</center></html>",
                "<html><center>Preguntas muy limitadas. Un verdadero reto.</center></html>"
        };

        for (int i = 0; i < dificultades.length; i++) {
            JPanel cardDificultad = new JPanel(new BorderLayout(0, 10));
            cardDificultad.setOpaque(false);

            JButton btnDificultad = new JButton(dificultades[i]);
            btnDificultad.setBackground(MAIN_BG_COLOR);
            btnDificultad.setForeground(TEXT_COLOR);
            btnDificultad.setFont(new Font("Monospaced", Font.BOLD, 13));
            btnDificultad.setFocusPainted(false);
            btnDificultad.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDificultad.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 0, 8, 0)
            ));

            btnDificultad.addActionListener(e -> {
                String txtBtn = btnDificultad.getText();
                Dificultad dificultad = Dificultad.NOSELECCIONADO;
                switch (txtBtn) {
                    case "FACIL" -> dificultad = Dificultad.FACIL;
                    case "NORMAL" -> dificultad = Dificultad.NORMAL;
                    case "DIFICIL" -> dificultad = Dificultad.DIFICIL;
                }
                casoService.actualizarDificultad(idcaso,dificultad);
                dialogo.dispose();
            });

            JLabel lblDesc = new JLabel(descripciones[i], SwingConstants.CENTER);
            lblDesc.setForeground(TEXT_COLOR);
            lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));

            cardDificultad.add(btnDificultad, BorderLayout.NORTH);
            cardDificultad.add(lblDesc, BorderLayout.CENTER);
            panelOpciones.add(cardDificultad);
        }

        panelFondo.add(panelOpciones, BorderLayout.CENTER);
        dialogo.add(panelFondo);
        dialogo.setVisible(true);
    }

}