CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    description character varying(2000), -- NOT NULL,
    requestor_id bigint,
    create_dt timestamp without time zone NOT NULL,
    CONSTRAINT requests_pkey PRIMARY KEY (id),
    CONSTRAINT requests_requestor_id_fkey FOREIGN KEY (requestor_id)
        REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS items
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name character varying(512) NOT NULL,
    description character varying(2000) NOT NULL,
    is_available boolean NOT NULL,
    owner_id bigint,
    request_id bigint,
    CONSTRAINT items_pkey PRIMARY KEY (id),
    CONSTRAINT items_owner_id_fkey FOREIGN KEY (owner_id)
        REFERENCES users (id),
    CONSTRAINT items_request_id_fkey FOREIGN KEY (request_id)
        REFERENCES requests (id)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    start_dt timestamp without time zone,
    end_dt timestamp without time zone,
    item_id bigint,
    booker_id bigint,
    status character varying(64),
    CONSTRAINT bookings_pkey PRIMARY KEY (id),
    CONSTRAINT bookings_booker_id_fkey FOREIGN KEY (booker_id)
        REFERENCES users (id),
    CONSTRAINT bookings_item_id_fkey FOREIGN KEY (item_id)
        REFERENCES items (id)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    item_id bigint NOT NULL,
    author_id bigint NOT NULL,
    text character varying(2000) ,
    create_dt timestamp without time zone,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT comments_author_id_fkey FOREIGN KEY (author_id)
        REFERENCES users (id),
    CONSTRAINT comments_item_id_fkey FOREIGN KEY (item_id)
        REFERENCES items (id)
    );

