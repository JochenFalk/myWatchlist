DROP DATABASE IF EXISTS springsession;

DROP TABLE Email;
DROP TABLE Email_Users;
DROP TABLE Roles;
DROP TABLE Users;
DROP TABLE Watchlist;
DROP TABLE Watchlist_Users;
DROP TABLE SearchResults;
DROP TABLE MovieCast;
DROP TABLE MovieCrew;
DROP TABLE MovieSearch;
DROP TABLE MovieSearch_Users;

DROP ROLE IF EXISTS mwlsuper;

DROP DOMAIN usernameRegex;
DROP DOMAIN passwordRegex;
DROP DOMAIN emailRegex;
