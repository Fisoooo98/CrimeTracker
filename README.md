Markdown
# Crime Tracker 🕵️‍♂️📂

Crime Tracker es un juego de investigación criminal y deducción en formato de novela visual interactiva. El jugador asume el rol de un detective encargado de resolver complejos crímenes analizando el escenario, administrando recursos limitados e interrogando a sospechosos para descubrir la verdad antes de que se agoten sus oportunidades.

---

## 🚀 Características Principales

* **Sistema de Interrogatorios Dinámico:** Mecánica interactiva de preguntas y respuestas con sospechosos. Descubrir respuestas clave te otorgará pistas tangibles, y estas pistas a su vez desbloquearán nuevas líneas de interrogatorio con otros implicados.
* **Desbloqueo de Evidencias por Probabilidad:** Sistema aleatorio indexado según el nivel de dificultad seleccionado. Cada pregunta clave realizada tiene una probabilidad matemática de revelar una evidencia crucial para el caso.
* **Persistencia y Estado del Juego:** Conexión completa a base de datos para almacenar el progreso de la partida, control del estado de las investigaciones (*Resuelto*, *Pendiente*, *No Resuelto*) y un bloc de notas dinámico donde el jugador puede escribir sus deducciones en tiempo real.
* **Gestión de Perfil y Puntuación:** Registro del progreso del detective a través de un sistema de puntuación acumulativa y asignación de rangos (Tiers de investigación) basados en el éxito y la dificultad de los casos cerrados.

---

## 🛠️ Arquitectura y Tecnologías

El proyecto ha sido desarrollado siguiendo estándares profesionales de ingeniería de software para garantizar un código limpio, modular y fácil de mantener:

* **Lenguaje de Programación:** Java SE 21.
* **Interfaz Gráfica de Usuario (GUI):** Java Swing (maquetación fluida basada en layouts, personalización de componentes y paleta de colores en modo oscuro).
* **Gestor de Base de Datos:** SQLite (para una persistencia ligera y portátil mediante JDBC).
* **Patrón de Diseño:** **MVC (Modelo-Vista-Controlador)**. Arquitectura desacoplada de tres capas:
  * **Model (Entities, DAO, Service):** Gestión de la lógica de negocio, entidades del dominio y acceso seguro a datos.
  * **View:** Componentes y ventanas de la interfaz gráfica independientes de la lógica.
  * **Controller:** Intermediario encargado de reaccionar a los eventos de la vista y actualizar el modelo.

---

## 💻 Requisitos Previos e Instalación

### Requisitos Técnicos
* **Java Development Kit (JDK):** Versión 21 o superior instalada y configurada en las variables de entorno del sistema.

### Instrucciones de Despliegue
1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/Fisoooo98/CrimeTracker.git](https://github.com/Fisoooo98/CrimeTracker.git)
Configurar la Base de Datos:

Asegúrate de que el archivo de la base de datos SQLite se encuentra en la ruta correcta especificada en la configuración del proyecto.

El script inicial creará automáticamente las tablas y cargará los registros necesarios de los casos si es la primera ejecución.

Compilar y Ejecutar:

Abre el proyecto en tu IDE de preferencia (IntelliJ IDEA, NetBeans, Eclipse).

Ejecuta la clase principal encargada de lanzar el hilo de la interfaz gráfica (java.awt.EventQueue.invokeLater).

##🎮 Guía de Uso / Cómo Jugar
Aceptar un Caso: Accede a la ventana VerCasos, examina los expedientes disponibles y selecciona uno. Antes de comenzar, deberás elegir el nivel de dificultad. A mayor dificultad, menor será el número de preguntas iniciales permitidas y más baja la probabilidad de descubrir evidencias automáticamente.

Iniciar la Investigación: Entra al panel de interrogatorios. Dispones de un contador limitado de preguntas. Utiliza tu lógica para interrogar a los sospechosos disponibles.

Analizar Hallazgos: Abre la ventana VerPistasAndEvidencias para consultar el progreso de tus descubrimientos y las pruebas materiales incriminatorias que has logrado desbloquear.

Bloc de Notas: Utiliza el sistema de notas integrado para apuntar teorías, contradicciones o descartar coartadas en cualquier momento de la partida.

Emitir el Veredicto: Cuando te quedes sin preguntas o tengas la certeza de quién es el culpable, dirígete al panel de acusación para señalar al asesino. Si aciertas, el caso pasará a estado Resuelto y sumarás puntos a tu expediente.

Consultar Perfil: Revisa la ventana Perfil para comprobar tu puntuación total acumulada y ver si has ascendido en el escalafón de los Tiers de detectives.
