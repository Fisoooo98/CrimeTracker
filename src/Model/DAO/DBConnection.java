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
                         contador_preguntas INTEGER default 5,
                         probevidencia INTEGER default 50,
                         jugando_ahora boolean default false,
                         estado TEXT NOT NULL DEFAULT 'NORESUELTO',
                         dificultad TEXT NOT NULL DEFAULT 'NOSELECCIONADO',
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
                    desbloqueada BOOLEAN DEFAULT 0,
                    id_caso INTEGER,
                    FOREIGN KEY (id_caso) REFERENCES Casos(id_caso) ON DELETE CASCADE
                );
                """);

            // 7. Tabla inventarios
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
            // INSERCIONES DE DATOS (DATA SEEDING)
            // ==========================================

            // ==========================================
            // CASO 1: El escaparate roto
            // Sospechosos: 1-3 | Preguntas default: 1-2 | Pistas: 1-3 | Evidencias: 1-3
            // Culpable: Kike (id 3)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (1, 'El escaparate roto',
                        'La pastelería Dulce Tentación amanece con el escaparate destrozado y la caja registradora vacía.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (1, 'Pedro (repartidor)',    'Dice que terminó su ruta a medianoche y se fue directo a casa.',          0, 1),
                (2, 'Ramona (dueña del bar de enfrente)', 'Estaba cerrando su local y vio la calle desierta.',         0, 1),
                (3, 'Kike (exempleado)',     'Fue despedido hace una semana y no tiene coartada sólida.',               1, 1);
                """);

            // Preguntas default del caso (sin requisito de pista)
            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (1, '¿A qué hora terminaste tu turno exactamente?',   NULL),
                (2, '¿Tenías algún conflicto con la pastelería?',      NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (1, 1, 'Terminé a medianoche, lo tiene registrado la empresa de logística.'),
                (1, 2, 'Ninguno, solo era un punto de mi ruta. Ni los conocía.'),
                (2, 1, 'Yo cerré el bar sobre las doce y media. La calle estaba tranquila cuando salí.'),
                (2, 2, 'Bueno, llevamos meses discutiendo por el uso de la terraza, pero nada grave.'),
                (3, 1, 'Estaba en casa. Solo. No puedo demostrarlo.'),
                (3, 2, 'Me echaron sin motivo después de tres años. Claro que estoy enfadado.');
                """);

            // Pista 1: visible desde inicio (sin conector)
            // Pistas 2 y 3: se desbloquean al preguntar a Ramona (p2) y a Kike (p2)
            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (1, 'El cristal fue golpeado desde fuera con un objeto romo.',                              1, NULL, NULL),
                (2, 'Hay una disputa de terrazas documentada entre Ramona y la pastelería.',                1, 2,    2),
                (3, 'Kike conocía los horarios exactos de recaudación de la caja.',                         1, 3,    2);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (1, 'Nota de despido con huellas dactilares encontrada en el suelo.',       1),
                (2, 'Fragmentos de cristal con dirección hacia dentro del escaparate.',     1),
                (3, 'Grabación borrosa de una cámara vecina con silueta masculina.',        1);
                """);


            // ==========================================
            // CASO 2: El atraco al quiosco
            // Sospechosos: 4-6 | Preguntas default: 3-4 | Pistas: 4-6 | Evidencias: 4-6
            // Culpable: Gustavo (id 6)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (2, 'El atraco al quiosco',
                        'El quiosco de prensa del parque fue robado en plena tarde. El quiosquero no sufrió violencia.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (4, 'Dolores (clienta habitual)',  'Estaba comprando el periódico justo antes, según ella.',                   0, 2),
                (5, 'Fran (repartidor)',           'Tenía una entrega en el edificio contiguo a esa hora.',                    0, 2),
                (6, 'Gustavo (vecino en paro)',    'Dice que estaba en casa, pero vive justo frente al parque.',               1, 2);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (3, '¿Tienes el albarán de la entrega?',        NULL),
                (4, '¿Necesitabas dinero urgentemente?',        NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (4, 3, 'No llevo albaranes, yo solo compro el periódico. No entiendo la pregunta.'),
                (4, 4, 'No especialmente, vivo de mi pensión y llego bien a fin de mes.'),
                (5, 3, 'Aquí lo tengo, aunque... bueno, le cambié la fecha porque me confundí al apuntarla.'),
                (5, 4, 'Tengo mis facturas al día, no necesito robar a nadie.'),
                (6, 3, 'No tengo ningún albarán, yo no reparto nada.'),
                (6, 4, 'Tengo algunas deudas pero las estoy gestionando. No es asunto suyo.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (4, 'El dinero fue sacado de un cajón no visible desde el exterior del quiosco.',    2, NULL, NULL),
                (5, 'El albarán de Fran tiene la fecha raspada y corregida.',                        2, 5,    3),
                (6, 'Gustavo tiene deudas de juego que vencían ese mismo día.',                      2, 6,    4);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (4, 'Cajón del quiosco forzado desde el interior de la cabina.',             2),
                (5, 'Albarán de entrega con fecha raspada y reescrita.',                     2),
                (6, 'Billete de apuestas a nombre de Gustavo encontrado en el suelo.',       2);
                """);


            // ==========================================
            // CASO 3: El incendio del almacén
            // Sospechosos: 7-9 | Preguntas default: 5-6 | Pistas: 7-9 | Evidencias: 7-9
            // Culpable: Isidro (id 9)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (3, 'El incendio del almacén',
                        'El almacén de una pequeña empresa textil arde de madrugada. Los bomberos sospechan fuego intencionado.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (7, 'Verónica (socia minoritaria)',  'Dormía en casa de su madre, hay testigos.',                                        0, 3),
                (8, 'Marcos (socio mayoritario)',    'Estaba de viaje en otra ciudad, conserva los tickets del hotel.',                  0, 3),
                (9, 'Isidro (cuñado de Marcos, encargado)', 'Dice que estaba en casa viendo la televisión. Nadie lo confirma.',         1, 3);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (5, '¿Quién tenía las llaves del almacén?',         NULL),
                (6, '¿Sabías que ibas a ser despedido?',            NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (7, 5, 'Yo nunca tuve llave del almacén, eso lo llevaba Marcos directamente.'),
                (7, 6, 'Nadie me iba a despedir, soy socia. Pero sé que Isidro sí tenía problemas.'),
                (8, 5, 'Las llaves las tengo yo y mi cuñado Isidro. Solo nosotros dos.'),
                (8, 6, 'No sé de qué habla. Isidro lleva años con nosotros.'),
                (9, 5, 'Marcos me dio una copia hace tiempo para las urgencias. Normal entre familia.'),
                (9, 6, '¡Eso es mentira! Nadie me dijo nada de ningún despido.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (7, 'El foco del incendio está en el archivo donde se guardan los contratos.',     3, NULL, NULL),
                (8, 'Solo Marcos e Isidro tenían llave del almacén.',                              3, 8,    5),
                (9, 'Isidro iba a ser despedido al día siguiente según documentos internos.',      3, 9,    6);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (7, 'Lata de gasolina vacía junto a la puerta trasera.',                     3),
                (8, 'Cerradura trasera abierta sin signos de fuerza.',                       3),
                (9, 'Carta de despido parcialmente quemada encontrada en el archivo.',       3);
                """);


            // ==========================================
            // CASO 4: El bolso desaparecido
            // Sospechosos: 10-12 | Preguntas default: 7-8 | Pistas: 10-11 | Evidencias: 10-12
            // Culpable: Sergio (id 12)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (4, 'El bolso desaparecido',
                        'En una boda, el bolso de la madre de la novia desaparece con 3.000 euros en efectivo destinados al catering.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (10, 'Lourdes (camarera del catering)',  'Estuvo toda la tarde sirviendo mesas, varios testigos.',                  0, 4),
                (11, 'Toni (fotógrafo)',                 'Sus fotos están geolocalizadas en el salón durante toda la tarde.',       0, 4),
                (12, 'Sergio (primo lejano de la novia)', 'Dice que estuvo en la barra todo el rato, pero el barman no lo recuerda.', 1, 4);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (7, '¿Fotografiaste la zona del guardarropa?',                             NULL),
                (8, '¿Preguntaste a alguien dónde se dejaban los objetos de valor?',       NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (10, 7, 'No, el guardarropa no forma parte del reportaje. Solo fotografío la ceremonia y el banquete.'),
                (10, 8, 'Yo no pregunto esas cosas, solo me dedico a servir.'),
                (11, 7, 'Sí, hice un par de fotos allí alrededor de las seis y media aproximadamente.'),
                (11, 8, 'No, yo no pregunto esas cosas. Tengo el mapa del evento y lo sigo.'),
                (12, 7, 'Yo no soy fotógrafo, no estaba por esa zona para nada.'),
                (12, 8, 'Bueno, le pregunté al barman dónde dejaba la gente las cosas de valor. Por si necesitaba guardar algo mío.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (10, 'Una foto de Toni muestra el bolso abierto en el guardarropa a las 18:42.',         4, 11, 7),
                (11, 'Sergio preguntó al barman dónde guardaba la gente los objetos de valor.',          4, 12, 8);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (10, 'Bolso vacío encontrado detrás de los contenedores del jardín.',           4),
                (11, 'Cerradura del guardarropa sin daños: alguien entró sin forzarla.',        4),
                (12, 'Servilleta con el número del hotel donde se alojaba Sergio.',             4);
                """);


            // ==========================================
            // CASO 5: El coche rayado
            // Sospechosos: 13-15 | Preguntas default: 9-10 | Pistas: 12-14 | Evidencias: 13-15
            // Culpable: Javier (id 15)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (5, 'El coche rayado',
                        'El coche nuevo del director del colegio aparece gravemente rayado en el aparcamiento del centro.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (13, 'Amparo (profesora de historia)',     'Estaba en una reunión de departamento hasta las 19h.',                       0, 5),
                (14, 'Bruno (conserje)',                   'Hizo la ronda de cierre pero dice que no vio nada raro.',                    0, 5),
                (15, 'Javier (padre de alumno sancionado)', 'Estuvo en el colegio a las 17h por reunión con el director, salió enfadado.', 1, 5);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (9,  '¿Cómo salió la reunión con el director?',              NULL),
                (10, '¿Funcionaban las cámaras del aparcamiento esa tarde?', NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (13, 9,  'Yo no tuve ninguna reunión con el director ese día. Estaba con mis compañeros.'),
                (13, 10, 'No lo sé, el aparcamiento no es mi zona.'),
                (14, 9,  'No asisto a las reuniones de dirección, yo me encargo de las instalaciones.'),
                (14, 10, 'Las desconecté un rato para hacer mantenimiento rutinario. Solo fue media hora.'),
                (15, 9,  'Fatal. No pienso tolerar que sancionen a mi hijo por una tontería. Se lo dije claro.'),
                (15, 10, 'No lo sé. Cuando salí ya era tarde y no me fijé en ninguna cámara.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (12, 'Las cámaras del aparcamiento estuvieron desactivadas de 17:30 a 18:00.',              5, NULL, NULL),
                (13, 'Javier y el director tuvieron una discusión muy fuerte antes del incidente.',          5, 15,   9),
                (14, 'Bruno desconectó las cámaras alegando mantenimiento rutinario en ese tramo horario.',  5, 14,   10);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (13, 'Marca de llaves a lo largo de toda la puerta del conductor.',                    5),
                (14, 'Registro de acceso del colegio: Javier salió a las 17:52.',                      5),
                (15, 'Botón de abrigo de cuero encontrado junto al coche.',                            5);
                """);


            // ==========================================
            // CASO 6: La firma falsificada
            // Sospechosos: 16-19 | Preguntas default: 11-12 | Pistas: 15-18 | Evidencias: 16-18
            // Culpable: Valentín (id 18)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (6, 'La firma falsificada',
                        'Un contrato millonario fue firmado con una identidad robada. La empresa descubre el fraude al tramitar el pago.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (16, 'Rebeca (abogada interna)',        'Revisó el contrato y lo dio por bueno. Alega documentación correcta.',           0, 6),
                (17, 'Nicolás (director comercial)',    'Estaba presentando resultados ante el consejo ese día. Docenas de testigos.',     0, 6),
                (18, 'Valentín (comercial junior)',     'Dice que el cliente se identificó correctamente. No conserva copias.',            1, 6),
                (19, 'Pilar (secretaria de dirección)', 'Tramitó el papeleo administrativo pero sin potestad para alterar contratos.',    0, 6);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (11, '¿Quién gestionó el contacto con ese cliente?',      NULL),
                (12, '¿Por qué no guardaste la documentación original?',  NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (16, 11, 'El contacto lo llevó Valentín en exclusiva. Yo solo revisé lo que me presentaron.'),
                (16, 12, 'Yo no gestionaba los originales, eso era responsabilidad del comercial.'),
                (17, 11, 'Valentín lo trajo directamente. Dijo que era un cliente de confianza.'),
                (17, 12, 'Eso es una pregunta para Valentín, no para mí.'),
                (18, 11, 'Yo lo gestioné porque el cliente me contactó a mí directamente. Todo fue normal.'),
                (18, 12, 'No vi necesidad. El cliente me entregó los documentos en mano y parecían correctos.'),
                (19, 11, 'Valentín me entregó los papeles sin sobre oficial. Me pareció raro pero no dije nada.'),
                (19, 12, 'Yo recibo lo que me dan. No es mi función verificar procedencia.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (15, 'El contacto con el cliente fue gestionado únicamente por Valentín.',          6, 17, 11),
                (16, 'Valentín recibió una transferencia de 4.000 euros de cuenta anónima.',        6, 18, 12),
                (17, 'Los papeles llegaron sin sobre oficial de la empresa cliente.',                6, 19, 11),
                (18, 'Hubo presiones directivas para cerrar el contrato antes de fin de trimestre.', 6, 16, 11);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (16, 'Contrato con firma que no coincide con la grafología del titular.',          6),
                (17, 'Transferencia bancaria de 4.000 euros a la cuenta de Valentín.',             6),
                (18, 'Email interno donde Nicolás pide agilizar el cierre sea como sea.',          6);
                """);


            // ==========================================
            // CASO 7: El veneno en el invernadero
            // Sospechosos: 20-23 | Preguntas default: 13-14 | Pistas: 19-22 | Evidencias: 19-21
            // Culpable: Dña. Paz (id 22)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (7, 'El veneno en el invernadero',
                        'El reconocido botánico don Aurelio aparece muerto en su invernadero privado. El forense detecta un veneno vegetal.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (20, 'Celeste (sobrina y heredera)',       'Estaba en el teatro. Tiene las entradas.',                                     0, 7),
                (21, 'Honorato (jardinero de confianza)',  'Trabajó hasta las 18h. Hay registro de salida en la finca.',                   0, 7),
                (22, 'Dña. Paz (vecina, rival académica)', 'Niega haber pisado la finca en meses. Un testigo la vio cerca.',              1, 7),
                (23, 'Emilio (asistente de investigación)', 'Digitalizando archivos en la biblioteca universitaria hasta las 21h.',        0, 7);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (13, '¿Cuál era vuestra relación académica?',                             NULL),
                (14, '¿Notaste algo raro en las plantas los últimos días?',               NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (20, 13, 'Mi tío tenía muchos colegas. No conozco a todos sus rivales académicos.'),
                (20, 14, 'Yo no visito el invernadero habitualmente. Era su espacio privado.'),
                (21, 13, 'No sé nada de rivalidades. Yo me dedico a las plantas, no a los papeles.'),
                (21, 14, 'Sí, falta un frasco de extracto de acónito del armario del fondo. Lo noté hace dos días.'),
                (22, 13, 'Tuvimos discrepancias científicas, como cualquier académico. Nada personal.'),
                (22, 14, 'No he estado allí. No sé nada de sus plantas.'),
                (23, 13, 'Don Aurelio era brillante. Dña. Paz publicó un artículo plagiando su investigación principal.'),
                (23, 14, 'Las plantas estaban bien la última vez que estuve, hace tres días.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (19, 'Un testigo vio a una mujer mayor entrar por la cancela trasera a las 19:30h.',    7, NULL, NULL),
                (20, 'Falta un frasco de extracto de acónito del armario del invernadero.',             7, 21,   14),
                (21, 'Dña. Paz plagió la investigación principal de Aurelio el año pasado.',            7, 23,   13),
                (22, 'Don Aurelio cambió el testamento una semana antes, perjudicando a Celeste.',      7, 20,   13);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (19, 'Taza de té con restos de acónito en el escritorio del invernadero.',          7),
                (20, 'Cancela trasera de la finca sin cerrojo, forzada desde fuera.',               7),
                (21, 'Artículo de Paz con párrafos idénticos a los borradores de Aurelio.',         7);
                """);


            // ==========================================
            // CASO 8: El sabotaje en la carrera
            // Sospechosos: 24-27 | Preguntas default: 15-16 | Pistas: 23-26 | Evidencias: 22-24
            // Culpable: Dani (id 26)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (8, 'El sabotaje en la carrera',
                        'El ciclista favorito del campeonato regional cae en el inicio. Alguien manipuló su bicicleta.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (24, 'Óscar (mecánico del equipo)',     'Preparó la bicicleta esa mañana. La entregó en perfecto estado, jura.',          0, 8),
                (25, 'Nacho (segundo clasificado)',     'Estaba en la concentración oficial con todos los corredores.',                    0, 8),
                (26, 'Dani (técnico del equipo rival)', 'Dice que solo pasó por el box a devolver unas herramientas.',                    1, 8),
                (27, 'Cris (novia, también corredora)', 'Se estaba cambiando en los vestuarios femeninos durante ese tiempo.',            0, 8);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (15, '¿Quién más tuvo acceso a la bicicleta tras tu revisión?',               NULL),
                (16, '¿Por qué estabas en el box del equipo rival?',                          NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (24, 15, 'Tras mi revisión la dejé en el box. Vi a un técnico de otro equipo rondando por allí.'),
                (24, 16, 'Yo no estaba en ningún box rival. Estoy hablando de otro técnico que vi pasar.'),
                (25, 15, 'Yo no me acerqué a la bicicleta en ningún momento. Lo puede confirmar cualquier corredor.'),
                (25, 16, 'Yo no estuve en el box rival para nada. Estaba con el grupo.'),
                (26, 15, 'Solo entré a devolver una llave inglesa que me habían prestado. No toqué nada.'),
                (26, 16, 'Ya lo dije, a devolver una herramienta. Fue un momento.'),
                (27, 15, 'No lo sé. Yo llegué al box cuando ya casi era la hora de salida.'),
                (27, 16, 'No conozco a ese técnico. Pedro no me habló de ningún problema.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (23, 'Un técnico ajeno al equipo estuvo en el box sin autorización antes de la carrera.',   8, NULL, NULL),
                (24, 'Las huellas de Dani están sobre el manillar de la bicicleta.',                        8, 26,   16),
                (25, 'Dani es especialista certificado en frenos de carbono del mismo modelo.',             8, 26,   16),
                (26, 'Nacho perdió su patrocinio principal en favor de la víctima esta temporada.',        8, 25,   15);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (22, 'Cable de freno delantero cortado con precisión quirúrgica.',                          8),
                (23, 'Tarjeta de acceso de Dani registrada en el box rival a las 08:14.',                   8),
                (24, 'Herramienta de ajuste de frenos de carbono en el maletín de Dani.',                   8);
                """);


            // ==========================================
            // CASO 9: El manuscrito robado
            // Sospechosos: 28-31 | Preguntas default: 17-18 | Pistas: 27-30 | Evidencias: 25-27
            // Culpable: Mateo (id 31)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (9, 'El manuscrito robado',
                        'El único manuscrito original de un poeta local del siglo XIX desaparece del archivo municipal el día de su exposición.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (28, 'Herminia (archivera jefe)',             'Supervisó la instalación y fue a comer. La sala estuvo 20 min sin personal.',   0, 9),
                (29, 'Patricio (conservador freelance)',      'Fue contratado solo para la instalación. Terminó y firmó la salida.',           0, 9),
                (30, 'Sra. Fuentes (concejala de cultura)',   'Estaba dando una rueda de prensa sobre el evento cuando desapareció.',          0, 9),
                (31, 'Mateo (estudiante, voluntario)',        'Colocaba carteles en otra sala, coincide con la franja sin personal.',          1, 9);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (17, '¿Quién conocía la existencia de la copia de llave de emergencia?',    NULL),
                (18, '¿Por qué te ofreciste de voluntario para el evento?',                 NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (28, 17, 'Esa copia solo la conocíamos la concejala, el alcalde y yo. Nadie más debería saberlo.'),
                (28, 18, 'Los voluntarios los gestiona el departamento de cultura. Yo no los selecciono.'),
                (29, 17, 'Yo no sabía que existía ninguna copia extra. Solo usé la llave oficial.'),
                (29, 18, 'Me contrataron para instalar las vitrinas. Punto. No soy voluntario.'),
                (30, 17, 'Existe una copia de emergencia sin registrar en el inventario. Solo los más cercanos lo sabían.'),
                (30, 18, 'Los voluntarios fueron seleccionados por el departamento. Mateo se presentó motu proprio.'),
                (31, 17, 'No sé de ninguna copia. Yo solo vine a ayudar.'),
                (31, 18, 'Es mi período de investigación para el TFM. Este poeta es el tema de mi tesis.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (27, 'La vitrina no fue forzada: alguien usó una llave.',                                             9, NULL, NULL),
                (28, 'Existe una copia de llave de emergencia de la vitrina sin registrar.',                          9, 30,   17),
                (29, 'La mochila de Mateo activó el detector de metales al salir del archivo.',                       9, 31,   18),
                (30, 'Patricio tiene contactos documentados en el mercado negro de antigüedades.',                    9, 29,   17);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (25, 'Registro del detector de metales con la hora de salida de Mateo.',            9),
                (26, 'Email en el ordenador de Mateo con oferta de 45.000 euros por el documento.', 9),
                (27, 'Vitrina sin signos de fuerza: alguien usó una llave.',                        9);
                """);


            // ==========================================
            // CASO 10: El accidente de montaña
            // Sospechosos: 32-34 | Preguntas default: 19-20 | Pistas: 31-34 | Evidencias: 28-30
            // Culpable: Blanca (id 33)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (10, 'El accidente de montaña',
                        'Un montañero experto muere en una ruta que conoce de memoria. Las marcas de la cuerda no cuadran con un accidente.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (32, 'Félix (guía de montaña)',             'Iba delante abriendo camino. Oyó la caída pero no la vio.',               0, 10),
                (33, 'Blanca (compañera de cordada, socia)', 'Iba la última en la cuerda. La víctima se soltó sola, según ella.',      1, 10),
                (34, 'Roque (tercer montañero del grupo)',   'Iba en el centro. No pudo ver lo que ocurrió en la parte trasera.',      0, 10);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (19, '¿Cuándo revisaste tu tramo de la cuerda?',                   NULL),
                (20, '¿Notaste algo raro en el comportamiento de Blanca?',         NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (32, 19, 'Revisé toda la cuerda antes de salir. Estaba en perfecto estado.'),
                (32, 20, 'Blanca paró el grupo con una excusa justo en el tramo más peligroso. Me pareció raro.'),
                (33, 19, 'La revisé por la mañana. Parecía bien.'),
                (33, 20, 'No entiendo la pregunta. Estaba concentrada en la ruta.'),
                (34, 19, 'No revisé la cuerda yo directamente, confié en los demás.'),
                (34, 20, 'Sí, Blanca se detuvo sin motivo aparente justo antes de la caída. Fue extraño.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (31, 'La cuerda en el lado de Blanca fue cortada con filo, no desgastada.',                       10, NULL, NULL),
                (32, 'Blanca es la única beneficiaria del seguro de vida de la víctima por 200.000 euros.',       10, 33,   19),
                (33, 'Blanca detuvo el grupo en el tramo más peligroso justo antes del accidente.',               10, 34,   20),
                (34, 'El mosquetón de la víctima estaba doblado por golpe previo, no por la caída.',              10, 32,   19);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (28, 'Fragmento de cuerda con corte limpio de cuchillo, sin desgaste.',                  10),
                (29, 'Póliza de seguro de vida con Blanca como única beneficiaria.',                     10),
                (30, 'Navaja en la mochila de Blanca con restos de fibra de cuerda.',                    10);
                """);


            // ==========================================
            // CASO 11: El fraude en la subasta
            // Sospechosos: 35-38 | Preguntas default: 21-22 | Pistas: 35-39 | Evidencias: 31-34
            // Culpable: Camilo (id 37)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (11, 'El fraude en la subasta',
                        'Un cuadro subastado por 180.000 euros resulta ser falso. Alguien sustituyó el original antes de la subasta.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (35, 'Dra. Inés (tasadora oficial)',            'Autenticó la obra tres días antes. Era original en ese momento, alega.',    0, 11),
                (36, 'Lorenzo (almacenista y transportista)',    'Transportó la obra el día de la subasta. Asegura que estaba sellada.',       0, 11),
                (37, 'Camilo (propietario, vendedor)',           'Entregó la obra hace dos semanas y no ha vuelto al almacén.',               1, 11),
                (38, 'Beatriz (representante casa de subastas)', 'Gestionó documentación pero no tuvo acceso físico a los almacenes.',        0, 11);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (21, '¿El precinto de seguridad estaba intacto cuando recogiste la obra?',            NULL),
                (22, '¿Cuál era tu situación económica antes de la subasta?',                         NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (35, 21, 'Cuando la certifiqué estaba perfecta. Lo que ocurrió después no es mi responsabilidad.'),
                (35, 22, 'Mi situación económica es irrelevante para este asunto.'),
                (36, 21, 'El precinto estaba intacto cuando la recogí. Hice una parada pequeña pero nadie tocó nada.'),
                (36, 22, 'Estoy bien económicamente. Trabajo fijo desde hace diez años.'),
                (37, 21, 'Yo ya no tenía acceso a la obra. La dejé en el almacén y ya está.'),
                (37, 22, 'Tengo algunas deudas como todo el mundo. Nada fuera de lo normal.'),
                (38, 21, 'Yo no manipulo los objetos físicamente. Solo gestiono la documentación.'),
                (38, 22, 'Camilo añadió una cláusula de indemnización si la obra no alcanzaba el precio mínimo. Raro.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (35, 'El cuadro tiene pigmentos acrílicos modernos; el original es óleo del siglo XX.',        11, NULL, NULL),
                (36, 'Lorenzo hizo una parada no registrada de 40 minutos durante el transporte.',             11, 36,   21),
                (37, 'Camilo tiene una deuda hipotecaria de 200.000 euros con vencimiento en 30 días.',        11, 37,   22),
                (38, 'El certificado de Inés tiene una firma ligeramente diferente a los anteriores.',         11, 35,   21),
                (39, 'Camilo añadió cláusula de indemnización inusual en el contrato de subasta.',             11, 38,   21);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (31, 'Cuadro con pigmentos acrílicos modernos (el original es óleo del siglo XX).',            11),
                (32, 'Precinto de la caja con signos de haber sido reabierto y vuelto a sellar.',              11),
                (33, 'Deuda hipotecaria de Camilo venciendo en 30 días desde la fecha de la subasta.',        11),
                (34, 'Cláusula contractual de indemnización a Camilo añadida de forma inusual.',               11);
                """);


            // ==========================================
            // CASO 12: El espía en el laboratorio
            // Sospechosos: 39-42 | Preguntas default: 23-24 | Pistas: 40-44 | Evidencias: 35-38
            // Culpable: Ignacio (id 40)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (12, 'El espía en el laboratorio',
                        'La fórmula de un medicamento experimental desaparece de un laboratorio farmacéutico de alta seguridad.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (39, 'Yolanda (investigadora principal)',  'Trabajó hasta las 22h según el log. Tiene acceso total al sistema.',          0, 12),
                (40, 'Ignacio (técnico de sistemas, TI)',  'Realizó mantenimiento esa noche. Único con acceso al log del servidor.',      1, 12),
                (41, 'Nadia (becaria de último año)',      'Se fue a las 18h con registro de salida. Hay una entrada lateral sin cámara.', 0, 12),
                (42, 'Sr. Cano (director general)',        'Cena de empresa con clientes internacionales. Veinte testigos.',              0, 12);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (23, '¿Notaste actividad inusual en el sistema esa semana?',                    NULL),
                (24, '¿Modificaste algún permiso de usuario o el log esa noche?',               NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (39, 23, 'Sí, vi dos accesos a mi carpeta en horarios en que yo no estaba. Pero el log aparece limpio.'),
                (39, 24, 'Yo no tengo acceso al log del servidor. Eso solo lo puede tocar Ignacio.'),
                (40, 23, 'Nada inusual. El sistema estaba funcionando con normalidad cuando hice el mantenimiento.'),
                (40, 24, 'Solo cambié un par de permisos de rutina. El log lo entregué completo.'),
                (41, 23, 'Yo me fui pronto. No sé lo que pasó después de las seis.'),
                (41, 24, 'Yo no tengo nivel de acceso para tocar nada de eso.'),
                (42, 23, 'Si hay un problema en el sistema debería haberse detectado antes. ¿Cómo es posible?'),
                (42, 24, 'Eso es una pregunta para el departamento de TI. Yo no gestiono los servidores.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (40, 'Hubo accesos al archivo de fórmulas desde el usuario de Yolanda en horarios fuera de su fichaje.', 12, NULL, NULL),
                (41, 'Ignacio eliminó 4 horas del log del servidor antes de entregarlo a seguridad.',              12, 40,   24),
                (42, 'Ignacio recibió un ingreso de 15.000 euros dos días después de la filtración.',              12, 40,   24),
                (43, 'Nadia buscó en Google cómo cifrar archivos el día anterior a la filtración.',                12, 41,   23),
                (44, 'Hace seis meses hubo un intento similar de espionaje que se archivó sin investigar.',        12, 42,   23);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (35, 'Log del servidor con 4 horas eliminadas por Ignacio.',                                  12),
                (36, 'Transferencia bancaria de 15.000 euros a la cuenta de Ignacio.',                        12),
                (37, 'Acceso remoto desde IP externa en la franja eliminada del log.',                        12),
                (38, 'USB en la taquilla de Ignacio con el cifrado exacto de la fórmula.',                    12);
                """);


            // ==========================================
            // CASO 13: El heredero envenenado
            // Sospechosos: 43-46 | Preguntas default: 25-26 | Pistas: 45-49 | Evidencias: 39-42
            // Culpable: Rodrigo (id 45)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (13, 'El heredero envenenado',
                        'El heredero de un gran holding familiar cae gravemente envenenado en su propio cumpleaños. Alguien adulteró su champán.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (43, 'Greta (madrastra, segunda esposa)',     'Organizó la fiesta personalmente. Visible toda la noche.',                 0, 13),
                (44, 'Damián (sumiller privado)',             'Preparó las copas según instrucciones y las dejó en bandeja.',             0, 13),
                (45, 'Rodrigo (abogado y asesor financiero)', 'Al otro lado del salón hablando con inversores durante el brindis.',       1, 13),
                (46, 'Lucía (hermanastra, hija de Greta)',    'En la terraza fumando con amigos durante el brindis.',                     0, 13);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (25, '¿De quién recibiste las instrucciones para la copa especial?',           NULL),
                (26, '¿Tienes algún interés personal en que el heredero no tome el control?', NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (43, 25, 'Yo no di ninguna instrucción especial para copas individuales. Eso es mentira.'),
                (43, 26, 'Yo quiero lo mejor para esta familia. El heredero es como un hijo para mí.'),
                (44, 25, 'Recibí una nota impresa indicando los detalles de la copa del cumpleañero. Sin firma.'),
                (44, 26, 'Yo solo sigo instrucciones. No tengo ningún interés en los negocios de la familia.'),
                (45, 25, 'Yo no envié ninguna nota al sumiller. No tengo nada que ver con la organización.'),
                (45, 26, 'Mi relación con el heredero es estrictamente profesional. No tengo ningún interés personal.'),
                (46, 25, 'Yo no di instrucciones de nada. Pregúntale a mi madre.'),
                (46, 26, 'Según el testamento actual yo no heredo nada. Pero eso no me convierte en sospechosa.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (45, 'La copa tenía un residuo oleoso en el borde no presente en las demás copas.',          13, NULL, NULL),
                (46, 'La nota de instrucciones fue impresa desde el despacho de Greta, sin firma.',          13, 44,   25),
                (47, 'Rodrigo gestiona el patrimonio en fideicomiso 10 años si el heredero muere antes de 30.', 13, 45, 26),
                (48, 'Rodrigo tiene acceso al despacho de Greta y pudo imprimir allí la nota.',              13, 45,   26),
                (49, 'Greta heredaría el 40% de la empresa si el heredero fallece antes que el patriarca.',  13, 43,   26);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (39, 'Copa con residuo de digoxina en concentración letal.',                                13),
                (40, 'Nota de instrucciones para el sumiller impresa desde el despacho de Greta.',          13),
                (41, 'Registro de acceso al despacho: Rodrigo entró dos horas antes de la fiesta.',         13),
                (42, 'Contrato de fideicomiso que da a Rodrigo el 2% del patrimonio anual durante 10 años.', 13);
                """);


            // ==========================================
            // CASO 14: El tren de medianoche
            // Sospechosos: 47-50 | Preguntas default: 27-28 | Pistas: 50-54 | Evidencias: 43-46
            // Culpable: Diana (id 49)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (14, 'El tren de medianoche',
                        'Un empresario es hallado muerto en su compartimento privado en un tren nocturno. La puerta estaba cerrada por dentro.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (47, 'Irene (secretaria personal)',    'En el vagón restaurante de 22h a 00:30h. Varios camareros lo confirman.',     0, 14),
                (48, 'Sr. Watanabe (socio japonés)',   'Dice que durmió desde las 22h. Nadie puede confirmarlo.',                    0, 14),
                (49, 'Diana (ex socia)',               'Viajaba en segunda clase, sin acceso teórico a los compartimentos privados.', 1, 14),
                (50, 'Héctor (revisor del vagón)',     'Hacía su ronda reglamentaria con registro de cada parada.',                  0, 14);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (27, '¿Sabías que ibas a ser despedida?',                                                NULL),
                (28, '¿Qué probabilidad hay de que coincidieseis en el mismo tren?',                     NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (47, 27, 'Sí. Me lo dijo esa misma mañana. Pero yo lo acepté con calma.'),
                (47, 28, 'No conozco a esa mujer. Nunca la había visto antes en este tren.'),
                (48, 27, 'Eso es un asunto entre él y su secretaria. No me incumbe.'),
                (48, 28, 'Yo no la conozco. Mi viaje estaba reservado desde hace semanas.'),
                (49, 27, 'No sé de qué secretaria hablan.'),
                (49, 28, 'Pura coincidencia. Necesitaba viajar y compré el primer billete disponible.'),
                (50, 27, 'No es información que yo maneje. Yo controlo el vagón, no los asuntos de los pasajeros.'),
                (50, 28, 'Hay una llave maestra para los compartimentos. Debería haber dos copias, pero una falta desde hace un mes.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (50, 'El revisor vio a alguien con gabardina oscura en el pasillo de compartimentos a las 23:15.', 14, NULL, NULL),
                (51, 'Irene iba a ser despedida al llegar al destino y lo sabía desde esa mañana.',               14, 47,   27),
                (52, 'Diana compró su billete cuatro horas antes que la víctima, anticipando el viaje.',          14, 49,   28),
                (53, 'Falta una llave maestra de compartimentos desde hace un mes.',                              14, 50,   28),
                (54, 'Diana tiene un pleito de 800.000 euros con vista al día siguiente del asesinato.',          14, 49,   28);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (43, 'Llave maestra de compartimentos encontrada en el bolso de Diana.',                     14),
                (44, 'Billete de Diana comprado 4 horas antes que el de la víctima en la misma agencia.',    14),
                (45, 'Pleito judicial de 800.000 euros con vista al día siguiente del asesinato.',           14),
                (46, 'Puerta del compartimento manipulable desde fuera con la llave maestra.',               14);
                """);


            // ==========================================
            // CASO 15: La red de las máscaras
            // Sospechosos: 51-54 | Preguntas default: 29-30 | Pistas: 55-59 | Evidencias: 47-50
            // Culpable: Conde R. (id 53)
            // ==========================================

            stmt.execute("""
                INSERT OR IGNORE INTO Casos (id_caso, titulo, descripcion, texto_notas, correcto)
                VALUES (15, 'La red de las máscaras',
                        'Durante un baile de máscaras benéfico, la presidenta de la fundación es envenenada. Todos llevaban máscara. Nadie sabe quién era quién.',
                        '', 0);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Sospechosos (id_sospechoso, nombre, coartada, es_culpable, id_caso) VALUES
                (51, 'Álvaro (exdirector financiero)',         'Asistió como invitado de un tercero. Su identidad no estaba en la lista.', 0, 15),
                (52, 'Valentina (directora artística, organizadora)', 'Gestionó el evento. Visible ante el personal toda la noche.',        0, 15),
                (53, 'Conde R. (aristócrata anfitrión)',        'Recibía invitados en la puerta. Hay 45 minutos sin testigos.',            1, 15),
                (54, 'Dra. Míriam (médica de la fundación)',    'Primera en atender a la presidenta al caer. Todos la vieron actuar.',     0, 15);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (29, '¿Quién sirvió la copa a la presidenta y quién tuvo acceso antes?',      NULL),
                (30, '¿Cuál era tu relación con la presidenta más allá del evento?',          NULL);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (51, 29, 'Yo no me acerqué a la presidenta en ningún momento. Apenas la conozco.'),
                (51, 30, 'Me despidió el año pasado alegando malversación. Una acusación falsa.'),
                (52, 29, 'Las copas las preparó el catering externo en la cocina. Yo no supervisé esa zona.'),
                (52, 30, 'Una relación profesional y de respeto mutuo. Era mi jefa en la fundación.'),
                (53, 29, 'Yo no entré en la cocina en ningún momento. Estaba recibiendo invitados.'),
                (53, 30, 'Una relación de colaboración larga. Llevamos años organizando este evento juntos.'),
                (54, 29, 'El catering sirvió las copas. Yo no tuve acceso a la cocina antes del evento.'),
                (54, 30, 'Era mi paciente y mi jefa. Su muerte me afecta profundamente.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Pistas (id_pista, texto_pista, id_caso, id_sospechoso_conector, id_pregunta_conectora) VALUES
                (55, 'La copa de la presidenta tenía aconitina. El catering externo fue contratado ese mismo día.', 15, NULL, NULL),
                (56, 'La invitación de Álvaro fue falsificada; la presidenta lo había denunciado el mes anterior.', 15, 51,   30),
                (57, 'El catering oficial fue sustituido de urgencia por uno externo sin historial.',               15, 52,   29),
                (58, 'La presidenta iba a revelar la malversación del Conde en ese mismo evento.',                  15, 53,   30),
                (59, 'Una cámara capta al Conde entrando en la cocina de servicio 40 min antes del envenenamiento.', 15, 53,  30);
                """);

            // Pregunta secreta que se desbloquea con la Pista 59 (cámara)
            stmt.execute("""
                INSERT OR IGNORE INTO Preguntas (id_pregunta, texto_pregunta, id_pista_requisito) VALUES
                (31, 'Una cámara le sitúa en la cocina 40 minutos antes del envenenamiento. ¿Cómo lo explica?', 59);
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Respuestas (id_sospechoso, id_pregunta, texto_respuesta) VALUES
                (53, 31, 'Esa cámara no funciona bien, tiene fallos. Yo nunca entré en la cocina, es imposible.');
                """);

            stmt.execute("""
                INSERT OR IGNORE INTO Evidencias (id_evidencia, texto_evidencia, id_caso) VALUES
                (47, 'Copa de la presidenta con traza de aconitina en dosis letal.',                            15),
                (48, 'Grabación del Conde entrando en la cocina 40 minutos antes del envenenamiento.',          15),
                (49, 'Contrato del catering de urgencia firmado desde el email personal del Conde.',            15),
                (50, 'Documentos de la fundación con irregularidades que la presidenta iba a presentar esa noche.', 15);
                """);

            System.out.println("Base de datos iniciada con los 15 casos.");

        } catch (SQLException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}