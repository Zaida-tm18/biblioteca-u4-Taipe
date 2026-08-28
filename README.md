# Biblioteca U4 — proyecto base para la Unidad IV

Proyecto base de la asignatura **Aplicaciones Web** (quinto nivel, Carrera de Ingeniería de Software, UTEQ) para la **Unidad IV: Modelo Vista Controlador y Servicios Web**.

Todo lo correspondiente a las Unidades I a III **ya está implementado y probado**. Usted se dedica exclusivamente a lo de la Unidad IV, que está marcado en el código con etiquetas `TODO-U4-n`.

---

## 1. Matriz de versiones

| Componente | Versión | Verificado en |
|---|---|---|
| Java | 25 (LTS, septiembre 2025) | `pom.xml` → `java.version` |
| Spring Boot | 4.1.1 | Maven Central |
| Spring Framework | 7.0.9 | gestionado por el BOM de Spring Boot |
| Spring Data JPA | 4.1.x | gestionado por el BOM |
| Hibernate | 7.4.5.Final | gestionado por el BOM |
| Flyway | 12.4.0 | gestionado por el BOM |
| springdoc-openapi | 3.1.0 | línea 3.x = Spring Boot 4 |
| jjwt | 0.13.0 | Maven Central |
| Testcontainers | 2.0.5 | gestionado por el BOM |
| JUnit Jupiter | 6.0.3 | vía `spring-boot-starter-test` |
| PostgreSQL | 18 | `docker-compose.yml` |
| Redis | 8 | `docker-compose.yml` |
| Maven | 3.9.16 o superior | mínimo exigido por Spring Boot 4: 3.6.3 |

### Tres cambios de Spring Boot 4 que rompen el código de la Unidad III

1. **`spring-boot-starter-web` está obsoleto**: el artefacto correcto es `spring-boot-starter-webmvc`.
2. **Testcontainers 2.x renombró sus artefactos**: `testcontainers-junit-jupiter` y `testcontainers-postgresql`, no `junit-jupiter` ni `postgresql`. La clase pasó de `org.testcontainers.containers.PostgreSQLContainer` a `org.testcontainers.postgresql.PostgreSQLContainer`.
3. **Spring Data JPA 4 ya no acepta `null` en `Specification`**: se usa `Specification.unrestricted()`.

---

## 2. Arranque

### Requisitos
- JDK 25 y Maven (IntelliJ IDEA los trae; verifique con `java -version` y `mvn -version`).
- Docker Desktop en ejecución (lo necesitan PostgreSQL, Redis y Testcontainers).

### Pasos

```bash
git clone <url-de-su-repositorio>
cd biblioteca-u4-base

cp .env.example .env
# Genere el secreto del JWT y péguelo en .env como JWT_SECRETO:
openssl rand -base64 48

docker compose up -d postgres redis
mvn clean verify
mvn spring-boot:run
```

| Recurso | URL |
|---|---|
| API | http://localhost:8080/api/v1/... |
| Swagger UI | http://localhost:8080/api/docs |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Salud | http://localhost:8080/actuator/health |

### Abrir en IntelliJ IDEA
`File → Open` y seleccione la carpeta del proyecto (no el `pom.xml` suelto). IntelliJ detecta Maven e importa las dependencias. Fije el SDK del proyecto en 25 desde `File → Project Structure → Project`.

---

## 3. Qué ya está hecho (Unidades I a III)

| Elemento | Ubicación |
|---|---|
| 7 entidades JPA con relaciones y reglas de dominio | `domain/` |
| Esquema versionado con Flyway y 3 migraciones | `resources/db/migration/` |
| Datos semilla: 54 libros, 40 socios, 24 préstamos, 3 usuarios | `V2` y `V3` |
| Repositorios Spring Data con paginación y `Specification` | `repository/` |
| Servicios con la lógica de negocio y sus reglas | `service/` |
| DTO de entrada y salida con Bean Validation | `web/dto/` |
| Mapeadores entidad → DTO | `web/mapper/` |
| Manejo uniforme de errores con **ProblemDetail (RFC 9457)** | `exception/GlobalExceptionHandler.java` |
| Envoltorio `ApiResponse {success, data, message, errors, meta}` | `web/dto/ApiResponse.java` |
| Caché Redis con dos *namespaces* y TTL diferenciados | `config/CacheConfig.java` |
| Cliente HTTP con *timeouts* acotados | `config/RestClientConfig.java` |
| **Controlador de referencia completo y comentado** | `web/controller/AutorController.java` |
| Base de pruebas de integración con Testcontainers | `test/BaseIntegracionTest.java` |
| **Plantilla del cuestionario teórico con sus enunciados** | `Cuestionario.md` |
| 6 pruebas de ejemplo que pasan | `test/repository/`, `test/web/` |
| Docker Compose con PostgreSQL 18, Redis 8 y la app | `docker-compose.yml` |

---

## 4. Qué debe hacer usted (Unidad IV)

Busque `TODO-U4` en IntelliJ (`Ctrl+Shift+F`) o abra la pestaña **TODO**.

| Etiqueta | Archivo | Objetivo de la Guía |
|---|---|---|
| `TODO-U4-1` | `LibroController`, `SocioController`, `PrestamoController` | OE2: API REST con envoltorio JSON |
| `TODO-U4-2` | `JwtService`, `JwtAuthenticationFilter`, `SecurityConfig`, `AuthController` | OE2: JWT stateless con roles |
| `TODO-U4-3` | `OpenApiConfig` | OE2: OpenAPI y Swagger UI |
| `TODO-U4-4` | `OpenLibraryClient` | OE3: API externa con caché y manejo de errores |
| `TODO-U4-5` | `LibroControllerIT` | OE1: mínimo 10 pruebas de integración |

**Empiece por `AutorController`**: está completo y comentado paso a paso; es el patrón exacto que debe replicar.

### Credenciales sembradas

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Admin123!` | ADMIN |
| `bibliotecario` | `Biblio123!` | BIBLIOTECARIO |
| `lector` | `Lector123!` | LECTOR |

Los hashes son BCrypt de coste 10, compatibles con `BCryptPasswordEncoder`.

---

## 5. Cuestionario teórico y protocolo de *commits*

### 5.1 `Cuestionario.md`

La parte teórica **se responde dentro del repositorio**, en el archivo `Cuestionario.md` de la raíz. Ya está creado con los enunciados y los bloques de respuesta vacíos.

- Responda debajo de cada pregunta, en el bloque `**Respuesta:**`.
- **No borre ni reescriba los enunciados**: el evaluador compara pregunta por pregunta.
- El archivo debe conservar su nombre exacto y su ubicación en la raíz.
- Las respuestas llegan por *commits* sucesivos, no en un único volcado final.

### 5.2 Protocolo de *commits*

**El docente indicará en voz alta los momentos de *commit* durante la sesión.** Cada vez que lo haga, usted ejecuta:

```bash
git add -A
git commit -m "checkpoint N: <que se hizo desde el anterior>"
git push
```

Dos reglas que se verifican después:

1. **Debe existir un *commit* por cada indicación del docente**, con marca de tiempo posterior a esa indicación y anterior a la siguiente.
2. **El salto de contenido entre dos *commits* consecutivos debe ser proporcional al tiempo transcurrido.** Un *commit* que introduce de golpe un apartado completo que no existía en el anterior —o que supera el umbral de líneas fijado por el docente— **se trata como falta grave**, porque es la firma de código traído de fuera de la sesión.

El evaluador lo comprueba así:

```bash
git log --pretty=format:'%h %ad %s' --date=iso
git log --numstat --pretty=format:'--- %h %ad' --date=iso
git diff --stat <commit_anterior> <commit_siguiente>
```

**Qué hacer para no caer en esto:** haga *commits* pequeños y frecuentes, incluso de código que todavía no compila del todo. Un historial de doce *commits* pequeños es evidencia a su favor; uno de dos *commits* enormes es evidencia en su contra, aunque el código sea suyo.

---

## 6. Reglas que la rúbrica verifica y suelen olvidarse

1. **Nunca mezcle los dos formatos**: éxito en `ApiResponse`, error en `ProblemDetail`. No devuelva `success:false` con estado 200.
2. **El secreto del JWT no se versiona.** Va en `.env`, que está en `.gitignore`. Un secreto en Git anula el esquema completo y la rúbrica lo penaliza.
3. **401 no es 403.** 401 = no autenticado; 403 = autenticado sin permisos.
4. **La creación devuelve 201 con cabecera `Location`**, no 200.
5. **Nunca cachee un fallo** de la API externa.
6. **El TTL de la caché se justifica** por la volatilidad del dato, no se elige al azar.
7. **Los `timeout` no son opcionales** al llamar a un servicio externo.
8. **Documente en el README** la cadena de compilación del informe LaTeX: es criterio de piso de la rúbrica.

---

## 7. Comandos útiles

```bash
make up            # levanta toda la pila
make test          # ejecuta las pruebas
make verify        # compila, prueba y genera cobertura JaCoCo
make psql          # consola SQL
make redis-keys    # evidencia de la caché de la API externa
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

---

## 8. Compilación del informe LaTeX (criterio de piso)

Cuando agregue su informe en `docs/`, documente aquí la cadena exacta. Por ejemplo:

```bash
cd docs
pdflatex informe.tex
bibtex   informe
pdflatex informe.tex
pdflatex informe.tex
```

Motor: `pdflatex`. Bibliografía: `bibtex`. Pasadas mínimas: 3. Sin esta documentación, y sin que el PDF se regenere desde el `.tex` clonando el repositorio, la calificación es CERO.
