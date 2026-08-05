#  Brick Breaker — Juego Arcade en Java

## Descripción

**Brick Breaker** es un juego arcade moderno y dinámico desarrollado en **Java** utilizando **Java Swing**. El jugador controla una paleta horizontal en la parte inferior de la pantalla y debe rebotar una pelota para destruir todos los ladrillos ubicados en la parte superior.

El juego cuenta con un estilo visual **Neon Arcade**, animación de partículas, estela de pelota, 5 niveles con patrones únicos, sistema de combos, menú de pausa in-game y un sistema de puntajes persistente almacenado en un archivo binario.

---

##  Cómo Jugar

### Controles

| Tecla / Acción | Descripción |
|----------------|-------------|
| `←` Flecha Izquierda | Mover paleta a la izquierda |
| `→` Flecha Derecha | Mover paleta a la derecha |
| `Espacio` | Lanzar la pelota al iniciar partida o perder una vida |
| `P` / `Escape` | Abrir / cerrar menú de pausa in-game |
| `Click Izquierdo` | Interactuar con los botones del menú de pausa y opciones |

### Reglas del Juego

1. Al iniciar una partida o tras perder una vida, la pelota aparecerá alineada sobre tu paleta. Presiona **Espacio** para lanzarla.
2. Mueve la paleta para evitar que la pelota caiga por la parte inferior de la pantalla.
3. La pelota rebota en las paredes lateral e inferior, en la paleta y en los ladrillos.
4. Al destruir un ladrillo, este soltará una **explosión de partículas** y se sumarán puntos a tu marcador.
5. Si logras golpear múltiples ladrillos seguidos sin perder la pelota, activarás el **Sistema de Combos** (hasta **x4 de multiplicador** de puntos).
6. Si la pelota cae, pierdes una **vida** y se reinicia el combo actual. Comienzas con **3 vidas**.
7. Al destruir todos los ladrillos de un nivel, avanzas al siguiente. ¡Completa los 5 niveles para lograr la **Victoria**!

---

---

##  Arquitectura del Proyecto

El proyecto sigue la arquitectura **MVC (Modelo-Vista-Controlador)**:

```
Juego Arcade/
├── src/
│   └── brickbreaker/
│       ├── Main.java                     # Punto de entrada de la aplicación
│       ├── model/                        # MODELO — Datos y lógica de negocio
│       │   ├── Ball.java                 # Modelo de la pelota (posición, velocidad, estela)
│       │   ├── Paddle.java               # Modelo de la paleta (movimiento, límites)
│       │   ├── Brick.java                # Modelo de ladrillos (resistencia HP, color, estado)
│       │   ├── BrickLayout.java          # Generación de 5 patrones de niveles
│       │   ├── GameState.java            # Estado (vidas, score, nivel, combos, pausa, victoria)
│       │   ├── Particle.java             # Partícula individual para efectos
│       │   ├── ParticleSystem.java       # Gestor y emisor de partículas
│       │   ├── ScoreEntry.java           # Entrada de puntaje (Serializable)
│       │   └── ScoreManager.java         # Lectura/escritura binaria de puntajes
│       ├── view/                         # VISTA — Interfaz gráfica Swing
│       │   ├── MainWindow.java           # Ventana principal (JFrame con CardLayout)
│       │   ├── MainMenuPanel.java        # Menú principal animado
│       │   ├── GamePanel.java            # Renderizado del juego, HUD, partículas y pausa
│       │   ├── ScoreBoardPanel.java      # Tabla de puntajes estilo arcade
│       │   ├── GameOverPanel.java        # Pantalla Game Over con efecto glitch
│       │   └── VictoryPanel.java         # Pantalla de victoria con fuegos artificiales
│       └── controller/                   # CONTROLADOR — Lógica e iteración
│           ├── GameController.java       # Game loop (~60 FPS), física, colisiones y eventos
│           └── InputHandler.java         # Captura de teclado y navegación
├── data/
│   └── scores.dat                        # Archivo binario de puntajes
├── .gitignore
└── README.md
```

---

##  Compilación y Ejecución

### Línea de comandos (PowerShell / Terminal)

**Compilar:**

```bash
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName

```

**Ejecutar:**

```bash
java -cp out brickbreaker.Main
```

---

