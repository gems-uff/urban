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
    id bigint NOT NULL,
    "time" timestamp without time zone NOT NULL,
    bus_id smallint,
    line_id smallint,
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
    id smallint NOT NULL,
    bus_number character varying(10),
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
-- Name: disposal_reasons; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE disposal_reasons (
    id smallint NOT NULL,
    name character varying(50),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: disposal_reasons_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE disposal_reasons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: disposal_reasons_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE disposal_reasons_id_seq OWNED BY disposal_reasons.id;


--
-- Name: disposals; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE disposals (
    id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    bus_id smallint,
    line_id smallint,
    loaded_file_id integer,
    "position" geography(Point,4326),
    last_postion_id integer,
    speed double precision NOT NULL,
    disposal_reason_id smallint NOT NULL,
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
    sequence_number smallint,
    line_id smallint,
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
    sequence_number smallint,
    line_id smallint,
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
    id smallint NOT NULL,
    line_number character varying(10),
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
    status smallint,
    errors character varying(255),
    type smallint,
    filename character varying(50),
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
-- Name: sys_configs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sys_configs (
    id integer NOT NULL,
    positions_hm_range_1 double precision DEFAULT (-15),
    positions_hm_range_2 double precision DEFAULT (-5),
    positions_hm_range_3 double precision DEFAULT 5,
    positions_hm_range_4 double precision DEFAULT 20,
    speed_hm_diff_range_1 double precision DEFAULT (-20),
    speed_hm_diff_range_2 double precision DEFAULT (-5),
    speed_hm_diff_range_3 double precision DEFAULT 5,
    speed_hm_diff_range_4 double precision DEFAULT 20,
    speed_hm_query_range_1 double precision DEFAULT 15,
    speed_hm_query_range_2 double precision DEFAULT 30,
    speed_hm_query_range_3 double precision DEFAULT 60,
    bounding_box_gap double precision DEFAULT 0.001,
    search_radius double precision DEFAULT 15,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: sys_configs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sys_configs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_configs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sys_configs_id_seq OWNED BY sys_configs.id;


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

ALTER TABLE ONLY disposal_reasons ALTER COLUMN id SET DEFAULT nextval('disposal_reasons_id_seq'::regclass);


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
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sys_configs ALTER COLUMN id SET DEFAULT nextval('sys_configs_id_seq'::regclass);


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
-- Name: disposal_reasons_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY disposal_reasons
    ADD CONSTRAINT disposal_reasons_pkey PRIMARY KEY (id);


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
-- Name: sys_configs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sys_configs
    ADD CONSTRAINT sys_configs_pkey PRIMARY KEY (id);


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
-- Name: index_buses_on_bus_number; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_buses_on_bus_number ON buses USING btree (bus_number);


--
-- Name: index_disposals_on_bus_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_bus_id ON disposals USING btree (bus_id);


--
-- Name: index_disposals_on_disposal_reason_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_disposal_reason_id ON disposals USING btree (disposal_reason_id);


--
-- Name: index_disposals_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_disposals_on_line_id ON disposals USING btree (line_id);


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

CREATE INDEX index_line_positions_on_position ON line_positions USING gist ("position");


--
-- Name: index_line_stops_on_line_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_stops_on_line_id ON line_stops USING btree (line_id);


--
-- Name: index_line_stops_on_position; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_line_stops_on_position ON line_stops USING gist ("position");


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

INSERT INTO schema_migrations (version) VALUES ('20140414031448');

INSERT INTO schema_migrations (version) VALUES ('20140414031449');

INSERT INTO schema_migrations (version) VALUES ('20140414031450');

INSERT INTO schema_migrations (version) VALUES ('20140922035218');

INSERT INTO schema_migrations (version) VALUES ('20140922035534');

INSERT INTO schema_migrations (version) VALUES ('20160228134001');

