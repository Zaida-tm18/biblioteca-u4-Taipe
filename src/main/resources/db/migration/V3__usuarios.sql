-- =====================================================================
--  V3: usuarios de demostracion para la Unidad IV.
--  Hashes BCrypt (coste 10, prefijo $2a$) compatibles con BCryptPasswordEncoder.
--  Credenciales de laboratorio, NO usar en produccion:
--    admin / Admin123!            -> ADMIN
--    bibliotecario / Biblio123!   -> BIBLIOTECARIO
--    lector / Lector123!          -> LECTOR
-- =====================================================================

INSERT INTO usuarios (username, password_hash, rol) VALUES
    ('admin', '$2a$10$u07GNfPwCpkyKHXP7VLZWuN/J2lSG6rxDeDZlUd5qbkls8If6adQO', 'ADMIN'),
    ('bibliotecario', '$2a$10$cDTbURMucu/kvyymOpuhiOqadjFY0KInOX2JAywvg0dClhUhLHJwW', 'BIBLIOTECARIO'),
    ('lector', '$2a$10$ZdUaOK/EQA4/PNv9O8JRiucti0Ey0meiDyhFrawCmE9.0XAU0I3N6', 'LECTOR');
