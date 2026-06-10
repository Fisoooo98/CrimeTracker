package Model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    // Cambia esto por tu variable de entorno o ruta correspondiente
    private final String url = "jdbc:sqlite:test.db";

    public void iniciarBaseDeDatos() {
        try (
                Connection conexion = DriverManager.getConnection(url);
                Statement stmt = conexion.createStatement();
        ) {
            stmt.execute("PRAGMA foreign_keys = ON;");

            // 1. Tabla Casos
            stmt.execute("""
                 CREATE TABLE IF NOT EXISTS Casos (
                         id_caso INTEGER PRIMARY KEY AUTOINCREMENT,
                         titulo TEXT NOT NULL,
                         descripcion TEXT,
                         jugando_ahora boolean default false,
                         estado TEXT NOT NULL DEFAULT 'NORESUELTO',
                         texto_notas TEXT DEFAULT '',
                         correcto BOOLEAN DEFAULT 0
                 );
                """);

            // 2. Tabla Sospechosos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Sospechosos (
                    id_sospechoso INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    coartada TEXT,
                    es_culpable BOOLEAN NOT NULL,
                    id_caso INTEGER,
                    FOREIGN KEY (id_caso) REFERENCES Casos(id_caso) ON DELETE CASCADE
                );
                """);

            // 3. Tabla Respuestas (Creada antes que Pistas y Preguntas para evitar conflictos de Foreign Keys cruzadas)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Respuestas (
                    id_sospechoso INTEGER,
                    id_pregunta INTEGER,
                    texto_respuesta TEXT NOT NULL,
                    PRIMARY KEY (id_sospechoso, id_pregunta),
                    FOREIGN KEY (id_sospechoso) REFERENCES Sospechosos(id_sospechoso) ON DELETE CASCADE,
                    FOREIGN KEY (id_pregunta) REFERENCES Preguntas(id_pregunta) ON DELETE CASCADE
                );
                """);

            // 4. Tabla Pistas (Conectada a Casos y a Respuestas)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Pistas(
                    id_pista INTEGER PRIMARY KEY AUTOINCREMENT,
                    texto_pista TEXT NOT NULL,
                    id_caso INTEGER,
                    id_sospechoso_conector INTEGER,
                    id_pregunta_conectora INTEGER,
                    FOREIGN KEY (id_caso) REFERENCES Casos(id_caso) ON DELETE CASCADE,
                    FOREIGN KEY (id_sospechoso_conector, id_pregunta_conectora) 
                        REFERENCES Respuestas(id_sospechoso, id_pregunta) ON DELETE SET NULL
                );
                """);

            // 5. Tabla Preguntas (Conectada a Pistas mediante id_pista_requisito)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Preguntas (
                    id_pregunta INTEGER PRIMARY KEY AUTOINCREMENT,
                    texto_pregunta TEXT NOT NULL,
                    id_pista_requisito INTEGER,
                    FOREIGN KEY (id_pista_requisito) REFERENCES Pistas(id_pista) ON DELETE SET NULL
                );
                """);

            // 6. Tabla Evidencias
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Evidencias (
                    id_evidencia INTEGER PRIMARY KEY AUTOINCREMENT,
                    texto_evidencia TEXT NOT NULL,
                    id_caso INTEGER,
                    FOREIGN KEY (id_caso) REFERENCES Casos(id_caso) ON DELETE CASCADE
                );
                """);
            //7. Tabla inventarios
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Inventario_Pistas (
                    id_caso INTEGER,
                    id_pista INTEGER,
                    PRIMARY KEY (id_caso, id_pista),
                    FOREIGN KEY (id_caso) REFERENCES Casos(id_caso) ON DELETE CASCADE,
                    FOREIGN KEY (id_pista) REFERENCES Pistas(id_pista) ON DELETE CASCADE
                );
                """);

            // ==========================================
            // INSERCIONES DE PRUEBA (DATA SEEDING)
            // ==========================================

            // 1. INSERTS DEL CASO 1: Joyería
            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto) 
                VALUES (1, 'El robo en la joyería', 'El Diamante Azul ha desaparecido de la caja fuerte central.', '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES 
                (1, 'Arturo (Gerente)', 'Dice que estaba en casa durmiendo, pero nadie puede confirmarlo.', 1, 1),
                (2, 'Clara (Dependienta)', 'Estaba cenando en un restaurante con tres testigos.', 0, 1);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES 
                (1, '¿Dónde se encontraba usted en el momento del crimen?', NULL),
                (2, '¿Qué relación tenía con la víctima o la escena del crimen?', NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES 
                (1, 1, 'Estaba en mi casa descansando del largo día de trabajo. No sé nada del diamante.'),
                (1, 2, 'Llevo 10 años gestionando la joyería, jamás tocaría la mercancía.'),
                (2, 1, 'Estaba en el restaurante "La Mamma" celebrando el cumpleaños de mi madre.'),
                (2, 2, 'Yo solo atiendo a los clientes, ni siquiera tengo la clave de la caja fuerte.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES 
                (1, 'La caja fuerte no fue forzada, se usó la combinación correcta.', 1, 1, 2);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES 
                (1, 'Un guante de terciopelo negro abandonado en el suelo de la trastienda.', 1);
                """);


            // 2. INSERTS DEL CASO 2: Hotel
            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto) 
                VALUES (2, 'Asesinato en el hotel', 'El huésped de la habitación 302 ha sido envenenado.', '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES 
                (3, 'Marta (Camarera)', 'Llevé el desayuno a las 8:00h y salí inmediatamente del hotel.', 0, 2),
                (4, 'Tomás (Socio)', 'Estaba en el vestíbulo esperando al dómine para una reunión de negocios.', 1, 2);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES 
                (3, 1, 'Estaba repartiendo el servicio de habitaciones en la cuarta planta en ese momento.'),
                (3, 2, 'Solo le serví el desayuno como cada mañana, parecía un hombre agradable.'),
                (4, 1, 'Llegué al hotel a las 8:15h y me quedé en la cafetería revisando unos informes financieros.'),
                (4, 2, 'Éramos socios comerciales, teníamos discrepancias sobre la venta de la empresa pero nada más.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES 
                (2, 'El veneno utilizado actúa en menos de una hora y se disuelve en líquidos calientes.', 2, 3, 2),
                (3, 'La taza de café de la víctima contenía restos de cianuro.', 2, 4, 2);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES 
                (2, 'Un frasco sospechoso en el carrito de limpieza del pasillo.', 2),
                (3, 'El registro de llamadas del teléfono de la habitación muestra un número desconocido.', 2);
                """);


            // 3. INSERTS DEL CASO 3: El Cuadro (Preparado para probar tu método dinámico)
            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto) 
                VALUES (3, 'El enigma del cuadro', 'Alguien ha cambiado el óleo original de la galería por una copia barata.', '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES 
                (5, 'Elena (Directora)', 'Estaba reunida con unos inversores extranjeros en su despacho.', 0, 3),
                (6, 'Sofía (Restauradora)', 'Salió antes porque tenía una cita médica, tiene el justificante.', 0, 3),
                (7, 'Roberto (Vigilante)', 'Hizo la ronda nocturna por el ala oeste, pero las cámaras de esa zona fallaron.', 1, 3);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES 
                (5, 1, 'En mi despacho, revisando los presupuestos del próximo trimestre con los patrocinadores.'),
                (5, 2, 'Ese cuadro era la joya de la corona de nuestra galería, una pérdida irreparable.'),
                
                (6, 1, 'Estaba en el médico. Me encontraba mal y pedí el alta firmada a las 19:30h.'),
                (6, 2, 'Yo misma restauré ese óleo el mes pasado. Conozco cada pincelada, ¡el que está colgado es un insulto!'),
                
                (7, 1, 'Estaba patrullando los pasillos. Es verdad que pasé por la sala del cuadro, pero no vi nada raro.'),
                (7, 2, 'No entiendo de arte, jefe. Para mí todos esos lienzos antiguos son iguales, no sé cuánto vale.');
                """);

            // Insertamos las pistas vinculadas a las respuestas iniciales
            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES 
                (5, 'El olor a pintura fresca todavía se percibe en la sala de exposición.', 3, 6, 2),
                (6, 'El interruptor de la alarma trasera fue saboteado desde el interior.', 3, 7, 1);
                """);

            //Preguntas secretas que piden como requisito la Pista 5 o la Pista 6
            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES 
                (3, 'Sofía mencionó que el cuadro falso huele a pintura fresca... ¿Sabe quién usa disolventes aquí?', 5),
                (4, 'Si la alarma se saboteó desde dentro en su turno, ¡usted tuvo que ver algo por fuerza!', 6);
                """);

            // Respuestas de los sospechosos a esas nuevas preguntas desbloqueables
            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES 
                (5, 3, '¿Disolventes? Solo el equipo de mantenimiento, o... bueno, el vigilante guarda sus herramientas en el aseo.'),
                (7, 4, '¡Es una acusación absurda! Yo no toqué ningún panel, ¡alguien debió colarse antes de mi guardia!');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES 
                (4, 'Un pelo largo y rubio enganchado en el marco del cuadro falso.', 3),
                (5, 'Un bote de disolvente escondido en los aseos del personal.', 3);
                """);

            System.out.println("Base de datos iniciada con datos de prueba estables.");

        } catch (SQLException e) {
            throw new RuntimeException("Error crítico al iniciar el juego: " + e.getMessage(), e);
        }
    }
}