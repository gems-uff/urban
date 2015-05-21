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

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: bus_positions; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE bus_positions (
    id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    bus_id integer,
    line_id integer,
    loaded_file_id integer,
    "position" geography(Point,4326),
    speed double precision NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: bus_positions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bus_positions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bus_positions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bus_positions_id_seq OWNED BY bus_positions.id;


--
-- Name: buses; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE buses (
    id integer NOT NULL,
    bus_number character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: buses_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE buses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: buses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE buses_id_seq OWNED BY buses.id;


--
-- Name: contatos; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contatos (
    id integer NOT NULL,
    name character varying(255),
    email character varying(255),
    telephone character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: contatos_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE contatos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contatos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE contatos_id_seq OWNED BY contatos.id;


--
-- Name: disposals; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE disposals (
    id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    bus_id integer,
    line_id integer,
    loaded_file_id integer,
    "position" geography(Point,4326),
    last_postion_id integer,
    speed double precision NOT NULL,
    disposal_reason character varying(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: disposals_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE disposals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: disposals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE disposals_id_seq OWNED BY disposals.id;


--
-- Name: line_positions; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE line_positions (
    id integer NOT NULL,
    sequence_number integer,
    line_id integer,
    description character varying(255),
    company character varying(255),
    loaded_file_id integer,
    "position" geography(Point,4326),
    shape_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: line_positions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE line_positions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: line_positions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE line_positions_id_seq OWNED BY line_positions.id;


--
-- Name: line_stops; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE line_stops (
    id integer NOT NULL,
    sequence_number integer,
    line_id integer,
    loaded_file_id integer,
    description character varying(255),
    company character varying(255),
    "position" geography(Point,4326),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: line_stops_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE line_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: line_stops_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE line_stops_id_seq OWNED BY line_stops.id;


--
-- Name: lines; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE lines (
    id integer NOT NULL,
    line_number character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: lines_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE lines_id_seq OWNED BY lines.id;


--
-- Name: loaded_files; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE loaded_files (
    id integer NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status integer,
    errors character varying(255),
    type integer,
    filename character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: loaded_files_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE loaded_files_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: loaded_files_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE loaded_files_id_seq OWNED BY loaded_files.id;


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE schema_migrations (
    version character varying(255) NOT NULL
);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bus_positions ALTER COLUMN id SET DEFAULT nextval('bus_positions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY buses ALTER COLUMN id SET DEFAULT nextval('buses_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY contatos ALTER COLUMN id SET DEFAULT nextval('contatos_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY disposals ALTER COLUMN id SET DEFAULT nextval('disposals_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY line_positions ALTER COLUMN id SET DEFAULT nextval('line_positions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY line_stops ALTER COLUMN id SET DEFAULT nextval('line_stops_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY lines ALTER COLUMN id SET DEFAULT nextval('lines_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY loaded_files ALTER COLUMN id SET DEFAULT nextval('loaded_files_id_seq'::regclass);


--
-- Name: bus_positions_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY bus_positions
    ADD CONSTRAINT bus_positions_pkey PRIMARY KEY (id);


--
-- Name: buses_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY buses
    ADD CONSTRAINT buses_pkey PRIMARY KEY (id);


--
-- Name: contatos_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contatos
    ADD CONSTRAINT contatos_pkey PRIMARY KEY (id);


--
-- Name: disposals_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY disposals
    ADD CONSTRAINT disposals_pkey PRIMARY KEY (id);


--
-- Name: line_positions_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY line_positions
    ADD CONSTRAINT line_positions_pkey PRIMARY KEY (id);


--
-- Name: line_stops_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY line_stops
    ADD CONSTRAINT line_stops_pkey PRIMARY KEY (id);


--
-- Name: lines_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY lines
    ADD CONSTRAINT lines_pkey PRIMARY KEY (id);


--
-- Name: loaded_files_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY loaded_files
    ADD CONSTRAINT loaded_files_pkey PRIMARY KEY (id);


--
-- Name: index_bus_positions_on_bus_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_bus_positions_on_bus_id ON bus_positions USING btree (bus_id);


--
-- Name: index_bus_positions_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_bus_positions_on_line_id ON bus_positions USING btree (line_id);


--
-- Name: index_bus_positions_on_position; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_bus_positions_on_position ON bus_positions USING gist ("position");


--
-- Name: index_bus_positions_on_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_bus_positions_on_time ON bus_positions USING btree ("time");


--
-- Name: index_bus_positions_on_time_and_bus_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_bus_positions_on_time_and_bus_id ON bus_positions USING btree ("time", bus_id);


--
-- Name: index_buses_on_bus_number; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_buses_on_bus_number ON buses USING btree (bus_number);


--
-- Name: index_disposals_on_bus_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_bus_id ON disposals USING btree (bus_id);


--
-- Name: index_disposals_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_line_id ON disposals USING btree (line_id);


--
-- Name: index_disposals_on_position; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_position ON disposals USING gist ("position");


--
-- Name: index_disposals_on_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_time ON disposals USING btree ("time");


--
-- Name: index_line_positions_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_positions_on_line_id ON line_positions USING btree (line_id);


--
-- Name: index_line_positions_on_position; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_positions_on_position ON line_positions USING btree ("position");


--
-- Name: index_line_stops_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_stops_on_line_id ON line_stops USING btree (line_id);


--
-- Name: index_line_stops_on_position; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_stops_on_position ON line_stops USING btree ("position");


--
-- Name: index_lines_on_line_number; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_lines_on_line_number ON lines USING btree (line_number);


--
-- Name: index_loaded_files_on_start_time_and_filename; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_loaded_files_on_start_time_and_filename ON loaded_files USING btree (start_time, filename);


--
-- Name: unique_schema_migrations; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX unique_schema_migrations ON schema_migrations USING btree (version);


--
-- PostgreSQL database dump complete
--

SET search_path TO public,postgis;

INSERT INTO schema_migrations (version) VALUES ('20140414031430');

INSERT INTO schema_migrations (version) VALUES ('20140414031440');

INSERT INTO schema_migrations (version) VALUES ('20140414031445');

INSERT INTO schema_migrations (version) VALUES ('20140414031449');

INSERT INTO schema_migrations (version) VALUES ('20140414031450');

INSERT INTO schema_migrations (version) VALUES ('20140922035218');

INSERT INTO schema_migrations (version) VALUES ('20140922035534');

INSERT INTO schema_migrations (version) VALUES ('20141215180720');

