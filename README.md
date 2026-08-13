Modelador de Ajedrez — organizado por microservicios (lógicos)
Modelo orientado a objetos de un tablero de ajedrez en Java, reorganizado en módulos independientes por bounded context. Cada módulo:

Es un proyecto Maven propio (pom.xml) que compila a su propio .jar.
Solo depende de los otros módulos declarados explícitamente como dependencia — nunca importa clases "por casualidad" de otro contexto.
Expone una fachada pública (XxxService) que es el único punto de entrada pensado para usarse desde fuera del módulo.
Hoy todo corre en el mismo proceso (sin servidores HTTP levantados), pero la separación en módulos + fachadas + contrato de solo lectura (EstadoTablero) está pensada para que, el día que se quiera dar el salto a microservicios de verdad, cada módulo se pueda sacar a su propio repositorio y ponerle una capa HTTP (Spring Boot, Javalin, Quarkus...) sin tocar su lógica interna.

Estructura del proyecto
ModeladorAjedrez/
├── pom.xml                        Agregador Maven de los 3 módulos
│
├── tablero-service/                BOUNDED CONTEXT: estado del tablero
│   ├── pom.xml
│   └── src/main/java/ajedrez/tablero/
│       ├── Color.java              Enum BLANCO / NEGRO
│       ├── Posicion.java           Casilla (fila, columna) + notación a1..h8
│       ├── Pieza.java              Clase abstracta base de toda pieza
│       ├── EstadoTablero.java      Contrato de SOLO LECTURA (interfaz)
│       ├── Tablero.java            Implementación mutable de EstadoTablero
│       └── TableroService.java     Fachada pública del módulo
│
├── movimientos-service/            BOUNDED CONTEXT: reglas de movimiento
│   ├── pom.xml                     (depende de tablero-service)
│   └── src/main/java/ajedrez/movimientos/
│       ├── CalculadorMovimientos.java  Interfaz que cumple cada pieza
│       ├── Peon.java
│       ├── Torre.java
│       ├── Caballo.java
│       ├── Alfil.java
│       ├── Reina.java
│       ├── Rey.java
│       └── MovimientosService.java     Fachada pública del módulo
│
├── partida-service/                BOUNDED CONTEXT: orquestación de la partida
│   ├── pom.xml                     (depende de tablero-service y movimientos-service)
│   └── src/main/java/ajedrez/partida/
│       ├── Jugada.java              DTO de una jugada ya ejecutada
│       ├── PartidaService.java      Fachada pública: turno, historial, capturas
│       └── Main.java                Punto de entrada de consola (demo)
│
└── interfaz-web/
    └── index.html                  Visualizador interactivo (independiente,
                                     no compila contra los módulos Java)
Por qué esta división y no otra
tablero-service es el único que sabe qué hay en cada casilla y sabe mutar esa información. Nadie más tiene permiso de escribir en el tablero directamente.
movimientos-service sabe las reglas de cada pieza, pero solo puede leer el tablero (recibe EstadoTablero, una interfaz sin métodos de escritura) — nunca podría, aunque quisiera, mover una pieza por su cuenta. Esto es justo el tipo de frontera que en un sistema real evitaría que el "servicio de reglas" corrompa el estado del "servicio de tablero".
partida-service es el único que conoce a los otros dos a la vez. Coordina: pide movimientos a movimientos-service, ejecuta el movimiento en tablero-service, decide si hubo captura y lleva el historial y el turno. Es, en términos de microservicios, el "servicio de negocio" que normalmente queda detrás de un API Gateway.
interfaz-web se mantiene aparte a propósito: hoy reimplementa las reglas en JavaScript (para no depender de un backend corriendo), pero el mapeo natural el día de mañana es que llame a los endpoints de partida-service en vez de recalcular las reglas en el navegador.
Compilar y ejecutar la demo
Con Maven (recomendado, respeta las dependencias entre módulos):

cd ModeladorAjedrez
mvn compile
mvn exec:java -pl partida-service -Dexec.mainClass=ajedrez.partida.Main
O manualmente con javac, compilando en orden de dependencia:

cd ModeladorAjedrez
mkdir -p out
javac -d out tablero-service/src/main/java/ajedrez/tablero/*.java
javac -d out -cp out movimientos-service/src/main/java/ajedrez/movimientos/*.java
javac -d out -cp out partida-service/src/main/java/ajedrez/partida/*.java
java -cp out ajedrez.partida.Main
Esto imprime el tablero inicial de la demo, los movimientos posibles de la Reina blanca, el tablero después de mover un peón, el turno actual y el historial de jugadas — el mismo comportamiento que la versión original de un solo módulo, ahora orquestado entre tres.

Usar la interfaz visual (HTML)
No requiere instalar nada ni compilar Java.

Abre interfaz-web/index.html con doble clic (o "Abrir con" tu navegador).
Haz clic en una pieza propia (empiezan las blancas) para ver sus movimientos posibles resaltados en el tablero.
Haz clic en una casilla resaltada para mover la pieza ahí. Si hay una pieza rival, se captura automáticamente.
El panel derecho muestra la ficha técnica de la pieza seleccionada: qué clase Java gobierna su movimiento (ahora en movimientos-service) y una descripción de la regla.
El registro de jugadas y las bandejas de piezas capturadas quedan debajo del tablero.
"Reiniciar tablero" vuelve a la posición inicial estándar.
La lógica de movimiento en el HTML es una traducción directa de cada movimientosPosibles(EstadoTablero) en Java (mismas condiciones, mismo orden de direcciones), así que el comportamiento en el navegador coincide con el modelo Java. No incluye jaque, enroque ni promoción de peón porque el modelo original tampoco los define.

Siguiente paso natural: exponer cada módulo por HTTP
La reorganización actual es "microservicios listos para separarse", no microservicios desplegados. Si más adelante se quiere dar el salto:

Agregar una dependencia web (Javalin o Spring Boot) al pom.xml de cada módulo y un controlador delgado que solo llame a la fachada (TableroService, MovimientosService, PartidaService) — la lógica de negocio no cambia.
Empaquetar cada módulo como su propio ejecutable (mvn package + Dockerfile por módulo) y agregar un docker-compose.yml que los levante juntos.
Cambiar interfaz-web/index.html para que, en vez de recalcular las reglas en JavaScript, llame a los endpoints de partida-service.
Opcional: un API Gateway delante de partida-service si se quiere un único punto de entrada para la interfaz web.
Posibles próximos pasos (funcionalidad del modelo)
Agregar detección de jaque y jaque mate (probablemente un nuevo bounded context, reglas-especiales-service, que consulte a tablero-service igual que movimientos-service).
Agregar enroque, captura al paso y promoción de peón.
Sumar pruebas unitarias sobre movimientosPosibles de cada pieza.
