--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.2
-- Dumped by pg_dump version 9.3.2
-- Started on 2014-10-13 10:17:25 PYST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 177 (class 3079 OID 11831)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2054 (class 0 OID 0)
-- Dependencies: 177
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 70684)
-- Name: bookmark; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE bookmark (
    id bigint NOT NULL,
    description character varying(255),
    link character varying(255)
);


ALTER TABLE public.bookmark OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 70692)
-- Name: permiso; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE permiso (
    id bigint NOT NULL,
    clave character varying(194),
    instancia character varying(64),
    operacion character varying(64),
    recurso character varying(64)
);


ALTER TABLE public.permiso OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 70697)
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rol (
    id bigint NOT NULL,
    descripcion character varying(100)
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 70702)
-- Name: rol_permiso; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rol_permiso (
    roles_id bigint NOT NULL,
    permisos_id bigint NOT NULL
);


ALTER TABLE public.rol_permiso OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 70707)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    activo boolean,
    apellido character varying(60),
    email character varying(100),
    nombre character varying(60),
    pwd character varying(64),
    telefono character varying(20),
    username character varying(25)
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 70712)
-- Name: usuario_rol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario_rol (
    usuarios_id bigint NOT NULL,
    roles_id bigint NOT NULL
);


ALTER TABLE public.usuario_rol OWNER TO postgres;

--
-- TOC entry 1925 (class 2606 OID 70691)
-- Name: bookmark_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY bookmark
    ADD CONSTRAINT bookmark_pkey PRIMARY KEY (id);


--
-- TOC entry 1927 (class 2606 OID 70696)
-- Name: permiso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY permiso
    ADD CONSTRAINT permiso_pkey PRIMARY KEY (id);


--
-- TOC entry 1931 (class 2606 OID 70706)
-- Name: rol_permiso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol_permiso
    ADD CONSTRAINT rol_permiso_pkey PRIMARY KEY (roles_id, permisos_id);


--
-- TOC entry 1929 (class 2606 OID 70701)
-- Name: rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);


--
-- TOC entry 1933 (class 2606 OID 70711)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 1935 (class 2606 OID 70716)
-- Name: usuario_rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT usuario_rol_pkey PRIMARY KEY (usuarios_id, roles_id);


--
-- TOC entry 1939 (class 2606 OID 70732)
-- Name: fk9481c4fe5c0e4796; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT fk9481c4fe5c0e4796 FOREIGN KEY (roles_id) REFERENCES rol(id);


--
-- TOC entry 1938 (class 2606 OID 70727)
-- Name: fk9481c4fedea2d2ad; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_rol
    ADD CONSTRAINT fk9481c4fedea2d2ad FOREIGN KEY (usuarios_id) REFERENCES usuario(id);


--
-- TOC entry 1936 (class 2606 OID 70717)
-- Name: fkf14d60e55c0e4796; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rol_permiso
    ADD CONSTRAINT fkf14d60e55c0e4796 FOREIGN KEY (roles_id) REFERENCES rol(id);


--
-- TOC entry 1937 (class 2606 OID 70722)
-- Name: fkf14d60e5d0585ffb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rol_permiso
    ADD CONSTRAINT fkf14d60e5d0585ffb FOREIGN KEY (permisos_id) REFERENCES permiso(id);


--
-- TOC entry 2053 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-10-13 10:17:25 PYST

--
-- PostgreSQL database dump complete
--
