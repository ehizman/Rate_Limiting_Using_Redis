--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: account; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE account (
    id integer NOT NULL,
    auth_id character varying(40),
    username character varying(30)
);


ALTER TABLE public.account OWNER TO postgres;

--
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_id_seq OWNER TO postgres;

--
-- Name: account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE account_id_seq OWNED BY account.id;


--
-- Name: phone_number; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE phone_number (
    id integer NOT NULL,
    number character varying(40),
    account_id integer
);


ALTER TABLE public.phone_number OWNER TO postgres;

--
-- Name: phone_number_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE phone_number_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.phone_number_id_seq OWNER TO postgres;

--
-- Name: phone_number_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE phone_number_id_seq OWNED BY phone_number.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY account ALTER COLUMN id SET DEFAULT nextval('account_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY phone_number ALTER COLUMN id SET DEFAULT nextval('phone_number_id_seq'::regclass);


--
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY account (id, auth_id, username) FROM stdin;
1	20S0KPNOIM	azr1
2	54P2EOKQ47	azr2
3	9LLV6I4ZWI	azr3
4	YHWE3HDLPQ	azr4
5	6DLH8A25XZ	azr5
\.


--
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('account_id_seq', 5, true);


--
-- Data for Name: phone_number; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY phone_number (id, number, account_id) FROM stdin;
1	4924195509198	1
2	4924195509196	1
3	4924195509197	1
4	4924195509195	1
5	4924195509049	1
6	4924195509012	1
7	4924195509193	1
8	4924195509029	1
9	4924195509192	1
10	4924195509194	1
11	31297728125	1
12	3253280312	1
13	3253280311	1
14	3253280315	1
15	3253280313	1
16	3253280329	1
17	441224459508	1
18	441224980086	1
19	441224980087	1
20	441224980096	1
21	441224980098	1
22	441224980099	1
23	441224980100	1
24	441224980094	2
25	441224459426	2
26	13605917249	2
27	441224459548	2
28	441224459571	2
29	441224459598	2
30	13605895047	2
31	14433600975	2
32	16052299352	2
33	13602092244	2
34	441224459590	2
35	441224459620	2
36	441224459660	2
37	234568266473	2
38	441224980091	2
39	441224980092	2
40	441224980089	2
41	441224459482	2
42	441224980093	2
43	441887480051	2
44	441873440028	2
45	441873440017	3
46	441970450009	3
47	441235330075	3
48	441235330053	3
49	441235330044	3
50	441235330078	3
51	34881254103	3
52	61871112946	3
53	61871112915	3
54	61881666904	3
55	61881666939	3
56	61871112913	3
57	61871112901	3
58	61871112938	3
59	61871112934	3
60	61871112902	3
61	61881666926	4
62	61871705936	4
63	61871112920	4
64	61881666923	4
65	61871112947	4
66	61871112948	4
67	61871112921	4
68	61881666914	4
69	61881666942	4
70	61871112922	4
71	61871232393	4
72	61871112916	5
73	61881666921	5
74	61871112905	5
75	61871112937	5
76	61361220301	5
77	61871112931	5
78	61871112939	5
79	61871112940	5
\.


--
-- Name: phone_number_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('phone_number_id_seq', 79, true);


--
-- Name: account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: phone_number_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY phone_number
    ADD CONSTRAINT phone_number_pkey PRIMARY KEY (id);


--
-- Name: phone_number_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY phone_number
    ADD CONSTRAINT phone_number_account_id_fkey FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump
--