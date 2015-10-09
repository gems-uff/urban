--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- Data for Name: disposal_reasons; Type: TABLE DATA; Schema: public; Owner: schettino
--


INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (1, 'Bus out of service', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (2, 'Distance is higher than 1.43 Kilometers', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (3, 'Record without line', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (4, 'Repeated record', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (5, 'Speed higher than 85.57 km/h', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (6, 'Invalid data', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (7, 'Data have not been updated', '2015-09-02 14:17:42', '2015-09-02 14:17:42');
INSERT INTO disposal_reasons (id, name, created_at, updated_at) VALUES (8, 'Bus at the garage', '2015-09-02 14:17:42', '2015-09-02 14:17:42');

--
-- Name: disposal_reasons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: schettino
--

SELECT pg_catalog.setval('disposal_reasons_id_seq', 7, true);


--
-- PostgreSQL database dump complete
--

