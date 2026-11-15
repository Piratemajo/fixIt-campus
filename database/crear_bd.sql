-- CREAR BASE DE DATOS


SET client_min_messages = warning;

DROP DATABASE IF EXISTS fixit_campus WITH (FORCE);

CREATE DATABASE fixit_campus
    ENCODING = 'UTF8'
    TEMPLATE = template0;

COMMENT ON DATABASE fixit_campus IS 'FixIt Campus: gestión de incidencias de mantenimiento del campus';
