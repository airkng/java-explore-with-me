DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE If EXISTS requests CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    participant_limit BIGINT NOT NULL,
    confirmed_requests BIGINT NOT NULL,
    --views BIGINT NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    title VARCHAR(120) NOT NULL,
    paid BOOLEAN NULL,
    request_moderation BOOLEAN NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE NULL,
    lat REAL,
    lon REAL,
    category_id BIGINT REFERENCES categories (category_id),
    initiator_id BIGINT REFERENCES users (user_id),
    state VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NULL,
    title VARCHAR(51) NULL,
    CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT REFERENCES events (event_id),
    requester_id BIGINT REFERENCES users (user_id),
    status VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	compilation_id BIGINT REFERENCES compilations (compilation_id),
	event_id BIGINT REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR NOT NULL,
    event_id BIGINT REFERENCES events (event_id) NOT NULL,
    author_id BIGINT REFERENCES users (user_id)  NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

