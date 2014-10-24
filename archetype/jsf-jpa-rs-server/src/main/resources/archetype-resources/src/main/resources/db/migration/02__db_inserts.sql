--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.2
-- Dumped by pg_dump version 9.3.2
-- Started on 2014-10-13 10:18:16 PYST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- TOC entry 2047 (class 0 OID 70684)
-- Dependencies: 170
-- Data for Name: bookmark; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO bookmark (id, description, link) VALUES (1, 'Demoiselle Portal', 'http://www.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (2, 'Demoiselle SourceForge', 'http://sf.net/projects/demoiselle');
INSERT INTO bookmark (id, description, link) VALUES (3, 'Twitter', 'http://twitter.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (4, 'Blog', 'http://blog.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (5, 'Wiki', 'http://wiki.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (6, 'Bug Tracking', 'http://tracker.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (7, 'Forum', 'http://forum.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (8, 'SVN', 'http://svn.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (9, 'Maven', 'http://repository.frameworkdemoiselle.gov.br');
INSERT INTO bookmark (id, description, link) VALUES (10, 'Downloads', 'http://download.frameworkdemoiselle.gov.br');


--
-- TOC entry 2058 (class 0 OID 0)
-- Dependencies: 176
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 5, true);


--
-- TOC entry 2048 (class 0 OID 70692)
-- Dependencies: 171
-- Data for Name: permiso; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (1, 'bookmark:leer', NULL, 'leer', 'bookmark');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (2, 'bookmark:crear', NULL, 'crear', 'bookmark');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (3, 'bookmark:modificar', NULL, 'modificar', 'bookmark');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (4, 'bookmark:eliminar', NULL, 'eliminar', 'bookmark');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (5, 'bookmark:listar', NULL, 'listar', 'bookmark');

INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (6, 'usuario:leer', NULL, 'leer', 'usuario');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (7, 'usuario:crear', NULL, 'crear', 'usuario');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (8, 'usuario:modificar', NULL, 'modificar', 'usuario');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (9, 'usuario:eliminar', NULL, 'eliminar', 'usuario');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (10, 'usuario:listar', NULL, 'listar', 'usuario');

INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (11, 'rol:leer', NULL, 'leer', 'rol');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (12, 'rol:crear', NULL, 'crear', 'rol');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (13, 'rol:modificar', NULL, 'modificar', 'rol');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (14, 'rol:eliminar', NULL, 'eliminar', 'rol');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (15, 'rol:listar', NULL, 'listar', 'rol');

INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (16, 'permiso:leer', NULL, 'leer', 'permiso');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (17, 'permiso:crear', NULL, 'crear', 'permiso');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (18, 'permiso:modificar', NULL, 'modificar', 'permiso');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (19, 'permiso:eliminar', NULL, 'eliminar', 'permiso');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (20, 'permiso:listar', NULL, 'listar', 'permiso');

/*INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (21, 'bookmark:*', NULL, '*', 'bookmark');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (22, 'usuario:*', NULL, '*', 'usuario');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (23, 'rol:*', NULL, '*', 'rol');
INSERT INTO permiso (id, clave, instancia, operacion, recurso) VALUES (24, 'permiso:*', NULL, '*', 'permiso');*/
--
-- TOC entry 2049 (class 0 OID 70697)
-- Dependencies: 172
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO rol (id, descripcion) VALUES (1, 'Admin');
INSERT INTO rol (id, descripcion) VALUES (2, 'User');
INSERT INTO rol (id, descripcion) VALUES (3, 'User Moderator');


--
-- TOC entry 2050 (class 0 OID 70702)
-- Dependencies: 173
-- Data for Name: rol_permiso; Type: TABLE DATA; Schema: public; Owner: postgres
--

-- Permisos del administrador: sobre todo
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 1);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 2);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 3);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 4);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 5);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 6);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 7);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 8);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 9);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 10);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 11);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 12);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 13);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 14);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 15);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 16);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 17);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 18);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 19);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (1, 20);

-- Permisos del usuario: sobre usuario, roles y permisos
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 6);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 7);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 8);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 9);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 10);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 11);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 12);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 13);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 14);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 15);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 16);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 17);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 18);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 19);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (2, 20);

-- Permisos del usuario moderarador: solo sobre bookmark
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (3, 1);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (3, 2);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (3, 3);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (3, 4);
INSERT INTO rol_permiso (roles_id, permisos_id) VALUES (3, 5);


--
-- TOC entry 2051 (class 0 OID 70707)
-- Dependencies: 174
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario (id, activo, apellido, email, nombre, pwd, telefono, username) VALUES (1, true, 'Administrator', 'admin@admin.com', 'Administrator', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '5441345', 'admin');
INSERT INTO usuario (id, activo, apellido, email, nombre, pwd, telefono, username) VALUES (2, true, 'User', 'user@user.com', 'User', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', '787454', 'user');
INSERT INTO usuario (id, activo, apellido, email, nombre, pwd, telefono, username) VALUES (3, true, 'User Mod', 'usermod@user.com', 'User Mod', '6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3', '787454', 'user2');


--
-- TOC entry 2052 (class 0 OID 70712)
-- Dependencies: 175
-- Data for Name: usuario_rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario_rol (usuarios_id, roles_id) VALUES (1, 1);
INSERT INTO usuario_rol (usuarios_id, roles_id) VALUES (2, 2);
INSERT INTO usuario_rol (usuarios_id, roles_id) VALUES (3, 3);


-- Completed on 2014-10-13 10:18:16 PYST

--
-- PostgreSQL database dump complete
--
