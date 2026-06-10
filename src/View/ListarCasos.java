package View;

import Controller.CasoController;
import Model.Entities.Caso;
import Model.Entities.Estado;
import Model.Service.CasoService;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class ListarCasos extends JFrame {
    CasoService casoService = new CasoService();
    private static final Color MAIN_BG_COLOR = new Color(18, 18, 20);
    private static final Color PANEL_BG_COLOR = new Color(26, 26, 30);
    private static final Color BORDER_COLOR = new Color(28, 28, 34);
    private static final Color TEXT_COLOR = new Color(170, 170, 175);
    private static final Color TITLE_COLOR = new Color(245, 240, 230);

    public ListarCasos() {
        setTitle("Listar Casos Resueltos");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initComponents() {
        JLabel titulo = new JLabel("CASOS RESUELTOS", SwingConstants.CENTER);
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(new Font("Georgia", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        List<Caso> casosNoResueltos = casoService.obtenerCasosporEstado(Estado.RESUELTO);

        List<Caso> casos = casosNoResueltos.stream()
                .sorted()
                .toList();

        JPanel panelCasos = new JPanel();
        panelCasos.setBackground(MAIN_BG_COLOR);
        panelCasos.setLayout(new GridLayout(0, 1, 0, 20));
        panelCasos.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        for (Caso caso : casos) {
            panelCasos.add(mostrarCasos(caso));
        }

        JScrollPane scroll = new JScrollPane(panelCasos);
        scroll.setBorder(null);
        scroll.setBackground(MAIN_BG_COLOR);
        scroll.getViewport().setBackground(MAIN_BG_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferiorVentana = new JPanel(new BorderLayout());
        panelInferiorVentana.setBackground(MAIN_BG_COLOR);
        panelInferiorVentana.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        JButton btnSalirVentana = new JButton("← Salir");
        btnSalirVentana.setBackground(PANEL_BG_COLOR);
        btnSalirVentana.setForeground(TEXT_COLOR);
        btnSalirVentana.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnSalirVentana.setFocusPainted(false);
        btnSalirVentana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalirVentana.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnSalirVentana.setPreferredSize(new Dimension(130, 40));
        btnSalirVentana.addActionListener(e -> dispose());

        panelInferiorVentana.add(btnSalirVentana, BorderLayout.WEST);
        add(panelInferiorVentana, BorderLayout.SOUTH);
    }

    public JPanel mostrarCasos(Caso caso) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBackground(PANEL_BG_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel tituloCaso = new JLabel("Caso #" + caso.getId_caso() + ": " + caso.getNombre(), SwingConstants.LEFT);
        tituloCaso.setForeground(TITLE_COLOR);
        tituloCaso.setFont(new Font("Monospaced", Font.BOLD, 16));
        panel.add(tituloCaso, BorderLayout.NORTH);

        JLabel descripcion = new JLabel(caso.getDescripcion(), SwingConstants.LEFT);
        descripcion.setForeground(TEXT_COLOR);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(descripcion, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);

        JLabel estado = new JLabel("ESTADO: " + caso.getEstado());
        estado.setForeground(TEXT_COLOR);
        estado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panelInferior.add(estado, BorderLayout.WEST);

        String textoCorrecto = (caso.isCorrecto()) ? "SÍ" : "NO";
        JLabel correcto = new JLabel("CORRECTO: " + textoCorrecto);
        correcto.setForeground(TEXT_COLOR);
        correcto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panelInferior.add(correcto, BorderLayout.EAST);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    static void main() {
        java.awt.EventQueue.invokeLater(() -> {
            new ListarCasos().setVisible(true);
        });
    }
}