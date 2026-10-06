-- Esquemas independientes por microservicio (arquitectura definida en el Avance 1, punto 4.3)
CREATE SCHEMA IF NOT EXISTS auth AUTHORIZATION sanhi_app;
CREATE SCHEMA IF NOT EXISTS inventory AUTHORIZATION sanhi_app;
CREATE SCHEMA IF NOT EXISTS sales AUTHORIZATION sanhi_app;

GRANT ALL ON SCHEMA auth TO sanhi_app;
GRANT ALL ON SCHEMA inventory TO sanhi_app;
GRANT ALL ON SCHEMA sales TO sanhi_app;
