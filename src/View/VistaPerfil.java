package View;

import Model.Entities.ResultadoPerfil;
import Model.Service.CasoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Pantalla que muestra el expediente del detective, incluyendo su rango, estadísticas de juego y avatar.
 */
public class VistaPerfil extends JFrame {
    private ResultadoPerfil resultadoPerfil;

    // Colores de la interfaz (Estilo policiaco / Noir)
    private static final Color MAIN_BG_COLOR = new Color(18, 18, 20);
    private static final Color PANEL_BG_COLOR = new Color(26, 26, 30);
    private static final Color CARD_BG = new Color(14, 14, 16);
    private static final Color BORDER_COLOR = new Color(35, 35, 42);
    private static final Color TEXT_COLOR = new Color(170, 170, 175);
    private static final Color TITLE_COLOR = new Color(245, 240, 230);
    private static final Color TEXT_GOLD = new Color(240, 225, 200);

    /**
     * Construye la ventana del perfil cargando los datos guardados del jugador.
     * * @param rs Objeto que contiene las estadísticas y la puntuación actual del detective.
     */
    public VistaPerfil(ResultadoPerfil rs) {
        this.resultadoPerfil = rs;
        setTitle("Perfil del Detective");
        setSize(850, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG_COLOR);

        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * Inicializa y organiza las secciones de la pantalla: cabecera, bloque de estadísticas y tarjeta lateral.
     */
    private void initComponents() {
        //Cabecera con Titulo Principal
        JLabel lblTitulo = new JLabel("EXPEDIENTE DEL DETECTIVE", SwingConstants.CENTER);
        lblTitulo.setForeground(TITLE_COLOR);
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        //Panel General de Informacion
        JPanel panelContenedorCentral = new JPanel(new BorderLayout(30, 0));
        panelContenedorCentral.setOpaque(false);
        panelContenedorCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        //Rango y Estadísticas
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        panelIzquierdo.add(crearPanelRango());
        panelIzquierdo.add(Box.createVerticalStrut(25));
        panelIzquierdo.add(crearPanelEstadisticas());

        panelContenedorCentral.add(panelIzquierdo, BorderLayout.CENTER);
        panelContenedorCentral.add(crearTarjetaFoto(), BorderLayout.EAST);

        add(panelContenedorCentral, BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    /**
     * Crea el bloque superior izquierdo que muestra el Tier y los puntos acumulados.
     * * @return El panel estructurado con los datos de nivel en formato JPanel.
     */
    private JPanel crearPanelRango() {
        JPanel panelRango = new JPanel(new GridLayout(1, 2, 20, 0));
        panelRango.setBackground(PANEL_BG_COLOR);
        panelRango.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panelRango.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblTier = new JLabel("TIER: " + resultadoPerfil.getTier());
        lblTier.setForeground(TEXT_GOLD);
        lblTier.setFont(new Font("Monospaced", Font.BOLD, 18));

        JLabel lblPuntuacion = new JLabel("PUNTUACIÓN: " + resultadoPerfil.getPuntuacion(), SwingConstants.RIGHT);
        lblPuntuacion.setForeground(TITLE_COLOR);
        lblPuntuacion.setFont(new Font("Monospaced", Font.BOLD, 15));

        panelRango.add(lblTier);
        panelRango.add(lblPuntuacion);

        return panelRango;
    }

    /**
     * Construye la tabla de rendimiento del jugador separada por dificultades (Fácil, Normal, Difícil).
     * * @return El panel con el desglose de casos resueltos en formato JPanel.
     */
    private JPanel crearPanelEstadisticas() {
        JPanel panelEstadisticas = new JPanel(new GridBagLayout());
        panelEstadisticas.setBackground(PANEL_BG_COLOR);
        panelEstadisticas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 15, 0);

        String[] dificultades = {"Facil", "Normal", "Dificil"};
        int fila = 0;

        for (String dif : dificultades) {
            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 2;
            JLabel lblDificultad = new JLabel(dif.toUpperCase());
            lblDificultad.setForeground(TEXT_GOLD);
            lblDificultad.setFont(new Font("Monospaced", Font.BOLD, 15));
            lblDificultad.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            panelEstadisticas.add(lblDificultad, gbc);
            fila++;

            //Casos Resueltos
            gbc.gridwidth = 1;
            gbc.gridy = fila;
            gbc.insets = new Insets(0, 10, 24, 0);

            JLabel lblResueltosTitulo = new JLabel("Casos resueltos");
            lblResueltosTitulo.setForeground(TEXT_COLOR);
            lblResueltosTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panelEstadisticas.add(lblResueltosTitulo, gbc);

            gbc.gridx = 0;
            gbc.gridy = fila;

            int casosResueltos = 0;
            int casosTotales = 0;
            switch (dif) {
                case "Facil" -> {
                    casosResueltos = resultadoPerfil.getCasosFacilesResueltos();
                    casosTotales = resultadoPerfil.getTotalCasosFaciles();
                }
                case "Normal" -> {
                    casosResueltos = resultadoPerfil.getCasosNormalesResueltos();
                    casosTotales = resultadoPerfil.getTotalCasosNormales();
                }
                case "Dificil" -> {
                    casosResueltos = resultadoPerfil.getCasosDificilesResueltos();
                    casosTotales = resultadoPerfil.getTotalCasosDificiles();
                }
            }

            gbc.gridx = 1;
            JLabel lblFallidosValor = new JLabel("-> " + casosResueltos + "/" + casosTotales, SwingConstants.RIGHT);
            lblFallidosValor.setForeground(TITLE_COLOR);
            lblFallidosValor.setFont(new Font("Monospaced", Font.BOLD, 14));
            panelEstadisticas.add(lblFallidosValor, gbc);
            fila++;

            gbc.insets = new Insets(0, 0, 15, 0);
        }

        return panelEstadisticas;
    }

    /**
     * Diseña el recuadro de identificación lateral derecho que contiene la silueta del detective.
     * * @return La tarjeta de foto del expediente en formato JPanel.
     */
    private JPanel crearTarjetaFoto() {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(CARD_BG);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        tarjeta.setPreferredSize(new Dimension(260, 0));

        JLabel lblIcono = new JLabel(generarIconoDetective(200, 200));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(lblIcono, BorderLayout.CENTER);

        JLabel lblID = new JLabel("ID: DETECTIVE_ACTIVO", SwingConstants.CENTER);
        lblID.setForeground(TEXT_COLOR);
        lblID.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblID.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        tarjeta.add(lblID, BorderLayout.SOUTH);

        return tarjeta;
    }

    /**
     * Construye la barra inferior de la ventana con el botón de regreso.
     * * @return El panel inferior en formato JPanel.
     */
    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));

        JButton btnSalir = new JButton("← Salir");
        btnSalir.setBackground(PANEL_BG_COLOR);
        btnSalir.setForeground(TEXT_COLOR);
        btnSalir.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnSalir.setPreferredSize(new Dimension(130, 40));
        btnSalir.addActionListener(e -> dispose());

        panelInferior.add(btnSalir, BorderLayout.WEST);
        return panelInferior;
    }

    /**
     * Dibuja utilizando vectores una silueta clásica de detective con sombrero y gafas oscuras.
     * * @param Ancho total del lienzo para el icono.
     * @param alto Alto total del lienzo para el icono.
     * @return El gráfico generado empaquetado en un ImageIcon.
     */
    private static ImageIcon generarIconoDetective(int ancho, int alto) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(ancho, alto, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Cuerpo / Gabardina
        g2.setColor(Color.BLACK);
        int cuerpoAncho = (int) (ancho * 0.80);
        int cuerpoAlto = (int) (alto * 0.50);
        int cuerpoX = (ancho - cuerpoAncho) / 2;
        int cuerpoY = alto - cuerpoAlto - 10;
        g2.fill(new RoundRectangle2D.Float(cuerpoX, cuerpoY, cuerpoAncho, cuerpoAlto, 40, 40));

        //Cabeza
        int cabezaDiametro = (int) (ancho * 0.50);
        int cabezaX = (ancho - cabezaDiametro) / 2;
        int cabezaY = (int) (cuerpoY - cabezaDiametro * 0.75);
        g2.fill(new Ellipse2D.Float(cabezaX, cabezaY, cabezaDiametro, cabezaDiametro));

        int centroX = ancho / 2;

        //Ala del sombrero
        int alaAncho = (int) (cabezaDiametro * 1.3);
        int alaAlto = 14;
        int alaX = centroX - (alaAncho / 2);
        int alaY = cabezaY + 10;
        g2.fill(new RoundRectangle2D.Float(alaX, alaY, alaAncho, alaAlto, 10, 10));

        //Copa del sombrero
        int copaAncho = (int) (cabezaDiametro * 0.9);
        int copaAlto = 45;
        int copaX = centroX - (copaAncho / 2);
        int copaY = alaY - copaAlto + 3;

        int[] xCopa = {copaX, copaX + 10, copaX + copaAncho - 10, copaX + copaAncho};
        int[] yCopa = {alaY + 2, copaY, copaY, alaY + 2};
        g2.fillPolygon(xCopa, yCopa, 4);

        //Gafas de incógnito
        g2.setColor(Color.WHITE);
        int centroCabezaX = cabezaX + (cabezaDiametro / 2);
        int centroCabezaY = cabezaY + (cabezaDiametro / 2) + 5;

        g2.fillRect(centroCabezaX - 30, centroCabezaY, 16, 6);
        g2.fillRect(centroCabezaX + 14, centroCabezaY, 16, 6);

        g2.dispose();
        return new ImageIcon(img);
    }

}