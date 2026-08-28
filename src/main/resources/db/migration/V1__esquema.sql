-- =====================================================================
--  V1: esquema base del sistema de biblioteca (Unidad III). NO modificar.
-- =====================================================================

CREATE TABLE autores (
    id            BIGSERIAL PRIMARY KEY,
    nombre        VARCHAR(150) NOT NULL,
    nacionalidad  VARCHAR(80)
);

CREATE TABLE editoriales (
    id      BIGSERIAL PRIMARY KEY,
    nombre  VARCHAR(150) NOT NULL,
    pais    VARCHAR(80)
);

CREATE TABLE categorias (
    id      BIGSERIAL PRIMARY KEY,
    nombre  VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE libros (
    id                     BIGSERIAL PRIMARY KEY,
    isbn                   VARCHAR(20)  NOT NULL UNIQUE,
    titulo                 VARCHAR(250) NOT NULL,
    anio_publicacion       INTEGER,
    ejemplares_totales     INTEGER      NOT NULL CHECK (ejemplares_totales >= 0),
    ejemplares_disponibles INTEGER      NOT NULL CHECK (ejemplares_disponibles >= 0),
    activo                 BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en              DATE         NOT NULL DEFAULT CURRENT_DATE,
    autor_id               BIGINT       NOT NULL REFERENCES autores (id),
    editorial_id           BIGINT       NOT NULL REFERENCES editoriales (id),
    categoria_id           BIGINT       NOT NULL REFERENCES categorias (id),
    CONSTRAINT chk_disponibles_no_supera_totales
        CHECK (ejemplares_disponibles <= ejemplares_totales)
);

CREATE INDEX idx_libros_titulo    ON libros (lower(titulo));
CREATE INDEX idx_libros_categoria ON libros (categoria_id);
CREATE INDEX idx_libros_activo    ON libros (activo);

CREATE TABLE socios (
    id              BIGSERIAL PRIMARY KEY,
    cedula          VARCHAR(10)  NOT NULL UNIQUE,
    nombre_completo VARCHAR(200) NOT NULL,
    correo          VARCHAR(150) NOT NULL UNIQUE,
    fecha_registro  DATE         NOT NULL DEFAULT CURRENT_DATE,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE prestamos (
    id                        BIGSERIAL PRIMARY KEY,
    libro_id                  BIGINT      NOT NULL REFERENCES libros (id),
    socio_id                  BIGINT      NOT NULL REFERENCES socios (id),
    fecha_prestamo            DATE        NOT NULL DEFAULT CURRENT_DATE,
    fecha_devolucion_prevista DATE        NOT NULL,
    fecha_devolucion_real     DATE,
    estado                    VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
);

CREATE INDEX idx_prestamos_socio_estado ON prestamos (socio_id, estado);
CREATE INDEX idx_prestamos_estado       ON prestamos (estado);

CREATE TABLE usuarios (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(60)  NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    rol           VARCHAR(20)  NOT NULL,
    activo        BOOLEAN      NOT NULL DEFAULT TRUE
);
