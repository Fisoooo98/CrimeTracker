package View;

import Controller.MenuController;
import Model.Entities.Caso;
import Model.Service.CasoService;
import View.InvestigacionUI.VentanaCaso;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    CasoService casoService = new CasoService();
    private static final Color MAIN_BG_COLOR = new Color(24, 24, 24); // Fondo principal oscuro
    private static final Color PANEL_BG_COLOR = new Color(36, 36, 36); // Fondo de los paneles interiores
    private static final Color BORDER_COLOR = new Color(50, 50, 50); // Color de borde suave
    private static final Color TEXT_COLOR = new Color(240, 240, 240); // Color de texto claro
    private static final Color TITLE_COLOR = new Color(200, 210, 220); // Color del título ligeramente azulado
    MenuController menuController = new MenuController();
    public MenuPrincipal() {
        setTitle("MenuPrincipal");

        setSize(500, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();

        getContentPane().setBackground(new Color(20, 20, 20));

        setVisible(true);

        setLayout(new BorderLayout());
    }

    private void initComponents() {



        //Titulo
        JLabel titulo = new JLabel("CRIME TRACKER", SwingConstants.CENTER);

        titulo.setForeground(new Color(230, 230, 230));
        titulo.setFont(new Font("Serif", Font.BOLD, 34));

        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        add(titulo, BorderLayout.NORTH);


        //Panel para los botones
        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 20));

        panel.setLayout(new GridLayout(0, 1, 10, 10));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));

        String[] textos = {
                "<html><center>Ver Casos</center></html>",
                "<html><center>Iniciar Investigación</center></html>",
                "<html><center>Listar Casos Resueltos</center></html>",
                "<html><center>Salir</center></html>"
        };

        for (String t : textos) {
            panel.add(crearBoton(t));
        }

        add(panel, BorderLayout.CENTER);
    }

    //Plantilla de boton.
    private JButton crearBoton(String textoHtml) {

        JButton btn = new JButton(textoHtml);

        btn.setBackground(new Color(25, 25, 25));
        btn.setForeground(Color.WHITE);

        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(220, 60));

        //hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 45, 45)); //más claro
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(25, 25, 25)); //color original
            }
        });

        //Click Listeners
        btn.addActionListener(e -> {

            String texto = btn.getText();

            if (texto.contains("Ver Casos")) {
                System.out.println("Has pulsado Ver Casos");
                menuController.accederAVentanaCasos();
            }

            else if (texto.contains("Iniciar Investigación")) {
                menuController.accederAlCaso(this);
            }

            else if (texto.contains("Listar Casos Resueltos")) {
                System.out.println("Has pulsado Listar Casos Resueltos");
                menuController.accederAListarCasos();
            }

            else if (texto.contains("Salir")) {
                System.out.println("Has pulsado Salir");
                System.exit(0);
            }

        });
        return btn;
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

    static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new MenuPrincipal();
        });
    }
}