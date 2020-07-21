package com.company.data.model;

public class ApplicationConfig {

    private String appHost;
    private int appPort;
    private String postgresUrl;
    private String postgresUser;
    private String postgresPassword;

    private static final String TMDB_API_KEY = "1c0a5b80ba526a387cb22c7d79fbeb03";
    private static final int TOKEN_EXPIRY_TIME = 60 * 60 * 24; // 24 hours
    private static final int LIST_EXPIRY_TIME = 60 * 60; // 1 hour
    private static final int SEARCH_EXPIRY_TIME = (60 * 60 * 24) * 7; // 1 week
    private static final int MAX_CAST_ITEMS = 12;
    private static final int MAX_LIST_ITEMS = 22;
    private static final int MAX_PAGE_NUMBER = 5;

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original";
    private static final String DEFAULT_POSTER_URL = "../resources/images/default-poster-332x500-noborders.png";
    private static final String BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/original";
    private static final String DEFAULT_BACKDROP_URL = "../resources/images/backdrop-3200x1800.jpg";
    private static final String BASE_CASTPOSTER_URL = "https://image.tmdb.org/t/p/original";
    private static final String DEFAULT_CASTPOSTER_URL = "../resources/images/profile-700x1050.png";
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/embed/";
    private static final String APPEND_VIDEO_URL = "?enablejsapi=1&amp;version=3&amp;playerapiid=ytplayer&amp;controls=0&amp;showinfo=0&amp;modestbranding=1&amp;rel=0&amp;loop=1";

    private static final String systemHash = "$2a$16$6NLk9MuDUDXqhrYIKurjUO49ScOVgz/zf74iPfzrAtoaD.oXR5k32";
    private static final String adminHash = "$2a$16$CMxiai7ls/GoyKGxZWdxxe/Lk6RuraDAkOL2PMJCu5XIRN9xIzuam";

    public ApplicationConfig() {
        this.appHost = "http://localhost:";
        this.appPort = 8080;
        this.postgresUrl = "jdbc:postgresql://localhost:5432/postgres";
        this.postgresUser = "mwlsuper";
        this.postgresPassword = "myWatchlistSuper=Super";
    }

    public String getAppHost() {
        return appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public int getAppPort() {
        return appPort;
    }

    public void setAppPort(int appPort) {
        this.appPort = appPort;
    }

    public String getPostgresUrl() {
        return postgresUrl;
    }

    public void setPostgresUrl(String postgresUrl) {
        this.postgresUrl = postgresUrl;
    }

    public String getPostgresUser() {
        return postgresUser;
    }

    public void setPostgresUser(String postgresUser) {
        this.postgresUser = postgresUser;
    }

    public String getPostgresPassword() {
        return postgresPassword;
    }

    public void setPostgresPassword(String postgresPassword) {
        this.postgresPassword = postgresPassword;
    }

    public static String getTmdbApiKey() {
        return TMDB_API_KEY;
    }

    public static int getTokenExpiryTime() {
        return TOKEN_EXPIRY_TIME;
    }

    public static int getListExpiryTime() {
        return LIST_EXPIRY_TIME;
    }

    public static int getSearchExpiryTime() {
        return SEARCH_EXPIRY_TIME;
    }

    public static int getMaxCastItems() {
        return MAX_CAST_ITEMS;
    }

    public static int getMaxListItems() {
        return MAX_LIST_ITEMS;
    }

    public static int getMaxPageNumber() {
        return MAX_PAGE_NUMBER;
    }

    public static String getBaseImageUrl() {
        return BASE_IMAGE_URL;
    }

    public static String getDefaultPosterUrl() {
        return DEFAULT_POSTER_URL;
    }

    public static String getBaseBackdropUrl() {
        return BASE_BACKDROP_URL;
    }

    public static String getDefaultBackdropUrl() {
        return DEFAULT_BACKDROP_URL;
    }

    public static String getBaseCastposterUrl() {
        return BASE_CASTPOSTER_URL;
    }

    public static String getDefaultCastposterUrl() {
        return DEFAULT_CASTPOSTER_URL;
    }

    public static String getBaseVideoUrl() {
        return BASE_VIDEO_URL;
    }

    public static String getAppendVideoUrl() {
        return APPEND_VIDEO_URL;
    }

    public static String getSystemHash() {
        return systemHash;
    }

    public static String getAdminHash() {
        return adminHash;
    }
}
