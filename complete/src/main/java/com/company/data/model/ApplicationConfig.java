package com.company.data.model;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

//    @Value("${app.data.defaultHost}")
    private String defaultHost = "http://localhost:";
//    @Value("${app.data.defaultPort}")
    private int defaultPort = 8080;
    private String host;
    private int port;

    private static final String systemHash = "$2a$16$6NLk9MuDUDXqhrYIKurjUO49ScOVgz/zf74iPfzrAtoaD.oXR5k32";
    private static final String adminHash = "$2a$16$CMxiai7ls/GoyKGxZWdxxe/Lk6RuraDAkOL2PMJCu5XIRN9xIzuam";

//    @Autowired
    public ApplicationConfig() {
        this.host = defaultHost;
        this.port = defaultPort;
    }

//    public ApplicationConfig(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static String getSystemHash() {
        return systemHash;
    }

    public static String getAdminHash() {
        return adminHash;
    }
}
