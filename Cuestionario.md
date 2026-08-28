# Cuestionario — Parte A del examen de la Unidad IV

> **Cómo se llena este archivo.** Responda **dentro de este mismo archivo**, debajo de cada pregunta, en el bloque marcado como `**Respuesta:**`. No borre ni reescriba los enunciados: el evaluador compara pregunta por pregunta. No añada ni quite secciones.
>
> **Este archivo se versiona en el repositorio.** Debe existir en la raíz, llamarse exactamente `Cuestionario.md`, y sus respuestas deben llegar por *commits* sucesivos hechos cuando el docente lo indique. Un archivo que aparece completo en un único *commit* al final de la sesión no cumple el protocolo y se trata según el criterio de piso 4 del examen.
>
> Se valora la precisión técnica y la justificación, **no la extensión**. Una respuesta correcta de seis líneas vale más que una página imprecisa. Cuando la pregunta pida referirse al proyecto base, hágalo con nombres concretos de clases o de *endpoints*.

---

## Datos del estudiante

| Campo | Valor |
|---|---|
| Apellidos y nombres | Taipe Mora Zaida Melissa|
| Número de carnet | 0604907956|
| Correo institucional | ztaipem@uteq.edu.ec|
| Fecha | 28/08/2026|
| URL del repositorio | |

---

## A1. Restricciones de REST aplicadas a un caso concreto — 8 puntos

**a) Enuncie las seis restricciones del estilo arquitectónico REST según Fielding. (3 puntos)**

**Respuesta:**
1. *Cliente-servidor*: separación de responsabilidades; el cliente (app web/móvil de la clínica) maneja la interfaz, y el servidor (biopet) el almacenamiento y la lógica de negocio veterinario, evolucionando cada lado por separado.
2. *Sin estado (stateless)*: cada petición del cliente debe contener toda la información necesaria para procesarla, sin que el servidor dependa de contexto guardado de peticiones anteriores.
3. *Cacheable*: las respuestas deben indicar si pueden almacenarse en caché, para reutilizarlas y reducir carga/latencia.
4. *Interfaz uniforme*: contrato consistente de acceso a los recursos (mascotas, propietarios, citas, historiales), con identificación de recursos, manipulación por representaciones y mensajes autodescriptivos.
5. *Sistema en capas*: el cliente no distingue si habla directamente con el servidor de biopet o con un intermediario (gateway, balanceador, proxy de caché).
6. *Código bajo demanda (opcional)*: el servidor puede enviar código ejecutable al cliente para extender su funcionalidad.



**b) El proyecto base expone `GET /api/v1/autores` y guarda el estado de la sesión del usuario solo en el JWT que el cliente envía en cada petición. Explique qué restricción concreta se está cumpliendo con esa decisión y qué consecuencia práctica tiene para escalar el sistema a varios servidores detrás de un balanceador. (3 puntos)**

**Respuesta:**
Que biopet exponga GET /api/v1/mascotas (o el recurso equivalente) y guarde el estado de sesión del usuario *solo en el JWT* enviado en cada petición cumple la restricción de *"sin estado" (stateless)*: el servidor no conserva sesión alguna del veterinario, recepcionista o dueño que usa el sistema entre una petición y otra.

Consecuencia práctica: al escalar biopet con *varias instancias detrás de un balanceador de carga, cualquier instancia puede atender cualquier petición del cliente, sin necesidad de *sticky sessions ni de compartir un almacén de sesiones entre nodos. Esto permite *escalar horizontalmente* (añadir o quitar réplicas del servicio de citas, historiales clínicos, etc.) sin afectar la continuidad de las sesiones activas de los usuarios de la clínica.


**c) De las seis restricciones, indique cuál es opcional y dé un ejemplo real de una API que la use. (2 puntos)**

**Respuesta:**
La restricción opcional es *código bajo demanda. Ejemplo real: aplicaciones que envían fragmentos de **JavaScript ejecutable al navegador* para ampliar la funcionalidad del cliente sin requerir una nueva versión instalada — un caso clásico citado es cómo *Google Maps* entrega scripts al cliente para renderizar y manipular mapas dinámicamente.


---

## A2. Anatomía y ciclo de vida de un JWT — 8 puntos

**a) Un JWT tiene tres partes separadas por puntos. Nómbrelas en orden e indique qué contiene cada una. (3 puntos)**

**Respuesta:**
1. *Header (encabezado)*: tipo de token (typ: JWT) y algoritmo de firma (alg: HS256, RS256, etc.).
2. *Payload (carga útil): los *claims, por ejemplo en biopet: sub (id del usuario), rol (VETERINARIO, RECEPCIONISTA, ADMIN), iat, exp, y quizás la clínica/sede asociada.
3. *Signature (firma)*: resultado de firmar base64url(header) + "." + base64url(payload) con una clave secreta/privada, para verificar integridad y autenticidad.


**b) Un compañero afirma: «como el JWT va firmado, puedo guardar en el *payload* la contraseña del usuario sin riesgo». Explique por qué está equivocado, precisando la diferencia entre firmar y cifrar. (2 puntos)**

**Respuesta:**
Está equivocado porque *firmar no es cifrar*:

- *Firmar* solo garantiza integridad y autenticidad (que nadie alteró el token y que lo emitió biopet), pero el payload sigue siendo *texto en claro codificado en Base64url*, legible por cualquiera sin ninguna clave (por ejemplo pegándolo en jwt.io).
- *Cifrar* sí oculta el contenido, haciéndolo ilegible sin la clave de descifrado (eso sería un JWE, no el JWS firmado típico).

Guardar la contraseña del usuario (dueño de mascota, veterinario, etc.) en el payload expone esa contraseña en texto claro a cualquiera que intercepte el token, lo inspeccione en el navegador o revise logs del sistema.


**c) El JWT es *stateless* por diseño, lo que genera un problema conocido: no se puede invalidar un token antes de que expire. Describa dos estrategias distintas para revocarlo y señale la desventaja de cada una. (3 puntos)**

**Respuesta:**
1. *Lista negra de tokens revocados* en un almacén rápido (Redis), con TTL igual al tiempo restante de expiración del token — por ejemplo, cuando se despide a un veterinario y se le quita el acceso inmediatamente.

 - Desventaja: reintroduce estado en el servidor (deja de ser 100% stateless) y añade una consulta extra a Redis en cada petición, más un punto de sincronización entre nodos.

2. *Access token de vida corta + refresh token revocable* (el access token expira en minutos; el refresh token se valida contra base de datos y puede eliminarse ahí).
 - Desventaja: mayor complejidad de implementación (dos tokens, endpoint de refresh, rotación); el access token ya emitido sigue siendo válido durante su corta ventana aunque se intente revocar antes.


---

## A3. SOAP frente a REST — 8 puntos

**a) Complete la tabla comparativa con seis criterios entre SOAP y REST. (5 puntos)**

**Respuesta:**

| Criterio | SOAP | REST |
|---|---|---|
| Formato del mensaje | XML estricto (envelope SOAP) | Flexible: JSON, XML, etc. |
| Contrato de descripción | WSDL (formal y estricto) | OpenAPI/Swagger (descriptivo, no obligatorio) |
| Sobrecarga de serialización | Alta (envelope, namespaces XML) | Baja (JSON ligero) |
| Tipado | Fuertemente tipado (XSD) | Débil/dinámico |
| Facilidad de consumo desde cliente móvil | Baja (payloads pesados, parsing costoso) | Alta (JSON nativo, menor consumo de batería/datos) |
| Manejo de errores | Elemento <Fault> estandarizado | Códigos de estado HTTP + cuerpo (ej. Problem Details) |

**b) El Servicio de Rentas Internas del Ecuador expone la autorización de comprobantes electrónicos mediante servicios SOAP. Explique dos razones técnicas por las que una institución de ese tipo mantiene SOAP en lugar de migrar a REST. (3 puntos)**

**Respuesta:**
1. *Seguridad avanzada y contratos formales verificables*: WS-Security permite firma digital y no repudio sobre los mensajes XML (relevante en comprobantes electrónicos), con validación estricta de esquemas (XSD) definidos en el WSDL; REST no tiene un equivalente igual de estandarizado y maduro.
2. *Transaccionalidad y entrega fiable: especificaciones WS- (WS-ReliableMessaging, WS-AtomicTransaction) dan garantías de entrega exactamente-una-vez y consistencia transaccional necesarias en sistemas fiscales críticos, algo que en REST habría que construir a medida.

(Nota aplicable por analogía a biopet: si biopet debiera integrarse con un sistema regulatorio o de facturación electrónica —como el SRI para las facturas de servicios veterinarios—, tendría que consumir esos servicios SOAP tal cual, aunque internamente use REST.)


---

## A4. Cache-aside sobre un servicio externo — 8 puntos

> El proyecto base define en `CacheConfig` dos espacios de caché: `libros` con TTL de 2 minutos y `openlibrary` con TTL de 24 horas.

**a) Describa el patrón *cache-aside* en sus cuatro pasos, desde que llega la petición hasta que se responde. (3 puntos)**

**Respuesta:**
1. Llega la petición (por ejemplo, consultar la ficha de una mascota o el detalle de una raza) y la aplicación *consulta primero la caché*.
2. Si hay *hit*, se devuelve el valor cacheado directamente, sin llamar a la base de datos ni al servicio externo.
3. Si hay *miss*, la aplicación consulta la fuente original (base de datos de biopet o la API externa de razas), obtiene el dato.
4. La aplicación *guarda el resultado en la caché* con su TTL correspondiente y *responde* al cliente.


**b) Justifique técnicamente por qué el TTL de `openlibrary` es doce veces mayor que el de `libros`, y qué criterio general debe guiar la elección de un TTL. (3 puntos)**

**Respuesta:**
razasapi es un servicio *externo y de datos poco volátiles* (características de una raza no cambian de un día a otro), y cada llamada implica latencia de red y posibles límites de uso del proveedor externo. Un TTL largo reduce drásticamente esas llamadas.

mascotas es *información propia del negocio* (peso, estado de vacunación, próxima cita, disponibilidad de turno) que cambia con frecuencia durante la operación diaria de la clínica; un TTL corto evita mostrar datos desactualizados (por ejemplo, un turno que ya fue tomado).

*Criterio general: el TTL debe elegirse según la **volatilidad del dato* frente al *costo de volver a obtenerlo* (latencia, carga, límites de la fuente externa). A mayor volatilidad, TTL más corto; a mayor costo de recomputar/obtener, TTL más largo tolerable.


**c) Explique por qué nunca debe almacenarse en caché la respuesta de un fallo del servicio externo, y describa qué le ocurriría al sistema si se hiciera. (2 puntos)**

**Respuesta:**
Si se cachea un error (timeout o 5xx de la API externa de razas), *todas las peticiones siguientes durante el TTL recibirán ese mismo error, aunque el servicio externo ya se haya recuperado — por ejemplo, ningún veterinario podría ver información de razas durante 24 horas aunque el proveedor externo vuelva a funcionar a los pocos minutos. Esto convierte una falla temporal en una **interrupción prolongada artificialmente*, degradando la disponibilidad sin posibilidad de auto-recuperación hasta que expire la caché.


---

## A5. Diagnóstico de códigos de estado y contrato de errores — 8 puntos

> Todos los errores del proyecto base salen en formato *Problem Details* conforme a la RFC 9457, que obsoleta a la RFC 7807.

Para cada escenario indique el código HTTP correcto y explique en una línea por qué. **Cada fila vale 1 punto** (0,5 por el código y 0,5 por la justificación); el literal g) vale 2 puntos.

| # | Escenario | Código | Justificación (una línea) |
|---|---|---|---|
| a | `GET /api/v1/libros/999999` y ese identificador no existe |*404 Not Found |el recurso identificado por ese id no existe en el servidor. |
| b | `POST /api/v1/libros` sin cabecera `Authorization` |*401 Unauthorized |no se identificó al solicitante; falta la credencial de autenticación. |
| c | Usuario autenticado con rol `LECTOR` envía `POST /api/v1/libros` |*403 Forbidden* |está identificado, pero su rol no tiene permiso para esa acción. |
| d | `POST /api/v1/libros` con el campo `titulo` vacío |*400 Bad Request* | la petición no cumple las reglas de validación del recurso. |
| e | Prestar un libro a un socio que ya tiene tres préstamos activos |*409 Conflict* |la petición es válida en forma pero choca con el estado actual del recurso según una regla de negocio. |
| f | La API de Open Library no responde dentro del *timeout* configurado |*504 Gateway Timeout* (o 502 Bad Gateway según el caso)|biopet actúa como intermediario hacia un servicio externo que no respondió a tiempo. |

**g) Explique por qué devolver `200 OK` con un cuerpo `{"success": false}` es un error de diseño, y qué restricción de REST se incumple al hacerlo. (2 puntos)**

**Respuesta:**
Porque *desacopla el significado real de la respuesta del código HTTP*: el cliente ya no puede confiar en el código de estado y debe abrir siempre el cuerpo para saber si, por ejemplo, la cita realmente se agendó o el registro de la mascota se guardó. Esto rompe herramientas estándar (caché, reintentos automáticos, monitoreo por código HTTP, proxies/balanceadores que interpretan el estado sin leer el payload).

Se incumple la restricción de *interfaz uniforme, en particular el principio de **mensajes autodescriptivos*: el código de estado HTTP debe bastar por sí mismo para describir el resultado de la operación; camuflar un error como 200 OK viola esa autodescriptividad.


---

## Declaración de honestidad académica

Marque con una `x` y complete:

- [x] Declaro que estas respuestas son de mi autoría, redactadas durante la sesión de examen, sin asistencia de inteligencia artificial ni comunicación con terceros.

Firma (nombre completo): Taipe Mora Zaida Melissa
