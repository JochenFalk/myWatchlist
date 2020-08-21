CREATE DOMAIN usernameRegex AS TEXT CHECK (VALUE ~* '^[a-zA-Z0-9_]{6,12}$');
CREATE DOMAIN passwordRegex AS TEXT CHECK (VALUE ~* '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$');
CREATE DOMAIN emailRegex AS TEXT CHECK (VALUE ~* '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$');

create user mwlsuper with password 'myWatchlistSuper=Super';

CREATE DATABASE springsession;
\connect springsession;

CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO mwlsuper;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO mwlsuper;

\connect postgres

CREATE TABLE Users(
	id BIGSERIAL,
	name usernameRegex NOT NULL,
	password VARCHAR(60) NOT NULL,
	emailAddress emailRegex NOT NULL UNIQUE,
	isValidated boolean NOT NULL,
	isRegistered boolean NOT NULL,
	validationAttempts int NOT NULL CHECK(validationAttempts <= 3),
	token varchar(60) NOT NULL,
	tokenExpiryDate timestamp NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE Roles(
	user_id bigint NOT NULL,
	role varchar(12) NOT NULL CHECK(role = 'User' OR role = 'Admin')
);

CREATE TABLE Email(
	id BIGSERIAL,
	type varchar(12) NOT NULL CHECK(type = 'Confirmation' OR type = 'Template' OR type = 'Custom'),
	subject varchar(100) NOT NULL,
	body text NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE Email_Users(
	email_id bigint NOT NULL,
	user_id bigint NOT NULL,
	PRIMARY KEY(email_id, user_id)
);

CREATE TABLE Watchlist(
	id BIGSERIAL,
	title varchar(15) NOT NULL,
	description varchar(35),
	listItems text NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE Watchlist_Users(
	watchlist_id bigint NOT NULL,
	user_id bigint NOT NULL,
	PRIMARY KEY(watchlist_id, user_id)
);

CREATE TABLE MovieSearch(
	id BIGSERIAL,
	title varchar(100) NOT NULL,
	year varchar(4),
	poster bytea,
	returnValue bigint NOT NULL,
	creationDate varchar(35),
	PRIMARY KEY(id)
);

CREATE TABLE MovieSearch_Users(
	search_id bigint NOT NULL,
	user_id bigint NOT NULL,
	PRIMARY KEY(search_id, user_id)
);

CREATE TABLE SearchResults(
	id BIGSERIAL,
	value text NOT NULL,
	search_id bigint NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(search_id) REFERENCES MovieSearch(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE MovieCast(
	id BIGSERIAL,
	value text,
	search_id int NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(search_id) REFERENCES MovieSearch(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE MovieCrew(
	id BIGSERIAL,
	value text,
	search_id int NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(search_id) REFERENCES MovieSearch(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO mwlsuper;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO mwlsuper;
